package pl.bloniarz.bis.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bloniarz.bis.externalapi.model.WowheadItemResponse;
import pl.bloniarz.bis.model.dao.item.ItemEntity;
import pl.bloniarz.bis.model.dao.item.StatsEquationEntity;
import pl.bloniarz.bis.model.dao.item.enums.ItemSlots;
import pl.bloniarz.bis.model.dao.item.enums.ItemTypes;
import pl.bloniarz.bis.model.dto.exceptions.AppErrorMessage;
import pl.bloniarz.bis.model.dto.exceptions.AppException;
import pl.bloniarz.bis.model.dto.response.item.*;
import pl.bloniarz.bis.repository.ItemRepository;
import pl.bloniarz.bis.repository.StatsEquationRepository;
import pl.bloniarz.bis.service.IItemService;
import pl.bloniarz.bis.service.ServicesUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService implements IItemService {

    private final ItemRepository itemRepository;
    private final StatsEquationRepository statsEquationRepository;
    private final ServicesUtil servicesUtil;

    @Override
    @Transactional
    public AddedItemsResponse addAllItemsToDatabase(List<WowheadItemResponse> wowheadItemResponseList, String dropInstance){
        List<ItemEntity> itemEntityList = itemRepository.saveAll(parseWowheadItemResponseListToItemEntityList(wowheadItemResponseList));
        return AddedItemsResponse.builder()
                .dropInstance(dropInstance)
                .items(itemEntityList.stream()
                        .map(this::parseItemEntityToAddedItem)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    @Transactional
    public ItemsForSlotResponse getItemsForSlot(ItemSlots slot, int itemLevel) {
        List <ItemEntity> itemEntityList = itemRepository.findBySlotAndOldIsFalse(slot);
        if(itemEntityList.isEmpty())
            throw new AppException(AppErrorMessage.SLOT_NOT_FOUND);

        return ItemsForSlotResponse.builder()
                .slot(slot.toString())
                .items(itemEntityList.stream()
                        .map(item -> servicesUtil.parseItemEntityToItem(item, itemLevel, 0, slot.toString()))
                        .collect(Collectors.toList()))
                .build();

    }

    @Override
    @Transactional
    public OldItemCollectionResponse setAllItemsFromDropInstanceToOld(String dropInstance) {
        List<ItemEntity> items = itemRepository.findByDropInstance(dropInstance);
        if(items.isEmpty())
            throw new AppException(AppErrorMessage.ITEM_NOT_FOUND);
        items = items.stream()
                .peek(itemEntity -> itemEntity.setOld(true))
                .collect(Collectors.toList());
        return OldItemCollectionResponse.builder()
                .dropInstance(dropInstance)
                .oldItems(items.stream()
                        .map(this::parseItemEntityToOldItem)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    @Transactional
    public OldItemResponse setItemWithSpecificIdToOld(long id) {
        ItemEntity item = itemRepository.findById(id)
                .orElseThrow(() -> new AppException(AppErrorMessage.ITEM_NOT_FOUND));
        item.setOld(true);
        return parseItemEntityToOldItem(item);
    }

    private AddedItem parseItemEntityToAddedItem(ItemEntity itemEntity){
        return AddedItem.builder()
                .id(itemEntity.getId())
                .name(itemEntity.getName())
                .slot(itemEntity.getSlot().toString())
                .type(itemEntity.getType())
                .wowheadLink(itemEntity.getWowheadLink())
                .build();
    }
    private OldItemResponse parseItemEntityToOldItem(ItemEntity itemEntity){
        return OldItemResponse.builder()
                .id(itemEntity.getId())
                .name(itemEntity.getName())
                .wowheadLink(itemEntity.getWowheadLink())
                .build();
    }
    private List<ItemEntity> parseWowheadItemResponseListToItemEntityList(List<WowheadItemResponse> wowheadItemResponseList){
        List<StatsEquationEntity> statsEquationEntities = statsEquationRepository.findAll();
        Map<String, StatsEquationEntity> equationsMap = new HashMap<>();

        statsEquationEntities.forEach(entity -> {
            equationsMap.put(entity.getName(), entity);
        });

        return wowheadItemResponseList.stream()
                .filter(item -> item.getQuality().equals("Rare") || item.getQuality().equals("Epic") || item.getQuality().equals("Legendary"))
                .map(item -> {
                    ItemTypes type = ItemTypes.fromName(item.getType());
                    ItemSlots slot = ItemSlots.extractSlotFromWowheadItem(item);
                    ItemEntity itemEntity = ItemEntity.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .type(type)
                            .slot(slot)
                            .dropInstance(item.getDropInstance())
                            .wowheadLink(item.getWowHeadLink())
                            .old(false)
                            .haste(item.getStats().getHaste())
                            .mastery(item.getStats().getMastery())
                            .criticalStrike(item.getStats().getCriticalStrike())
                            .versatility(item.getStats().getVersatility())
                            .build();

                    if(slot == ItemSlots.SHIELD){
                        itemEntity.setIntelligence(1.0);
                        itemEntity.setStrength(1.0);
                    }
                    else {
                        if (item.getStats().getIntelligence() != 0)
                            itemEntity.setIntelligence(1.0);
                        if (item.getStats().getStrength() != 0)
                            itemEntity.setStrength(1.0);
                        if (item.getStats().getAgility() != 0)
                            itemEntity.setAgility(1.0);
                    }
                    if(item.getStats().getStamina() != 0)
                        itemEntity.setStamina(1.0);
                    if(item.getStats().getSecondary() != 0)
                        itemEntity.setSecondary(1.0);

                    itemEntity.setStatsBothWay(findStatsEquationsDependingOnSlot(itemEntity.getSlot(), equationsMap));
                    return itemEntity;
                })
                .collect(Collectors.toList());
    }
    private List<StatsEquationEntity> findStatsEquationsDependingOnSlot(ItemSlots slot, Map<String,StatsEquationEntity> map) {
        List<StatsEquationEntity> retList = new LinkedList<>();

        boolean high = slot == ItemSlots.CHEST
                || slot == ItemSlots.HEAD
                || slot == ItemSlots.LEGS
                || slot == ItemSlots.TH_WEAPON
                || slot == ItemSlots.RANGED;

        boolean medium = slot == ItemSlots.WAIST
                || slot == ItemSlots.SHOULDERS
                || slot == ItemSlots.FEET
                || slot == ItemSlots.HANDS;

        boolean low = slot == ItemSlots.BACK
                || slot == ItemSlots.WRISTS;

        boolean amulet = slot == ItemSlots.NECK
                || slot == ItemSlots.FINGER;

        boolean thIntWeapon = slot == ItemSlots.TH_INT_WEAPON;
        boolean offHandInt = slot == ItemSlots.OFF_HAND_ITEM;
        boolean trinket = slot == ItemSlots.TRINKET;
        boolean ohWeapon = slot == ItemSlots.OH_WEAPON;
        boolean ohIntWeapon = slot == ItemSlots.OH_INT_WEAPON
                || slot == ItemSlots.RANGED_INT;
        boolean shield = slot == ItemSlots.SHIELD;

        if(high){
            retList.add(map.get("STAMINA_HIGH"));
            retList.add(map.get("MAIN_HIGH"));
            retList.add(map.get("SECONDARY_HIGH"));
        }
        else if(medium){
            retList.add(map.get("STAMINA_MEDIUM"));
            retList.add(map.get("MAIN_MEDIUM"));
            retList.add(map.get("SECONDARY_MEDIUM"));
        }
        else if(low){
            retList.add(map.get("STAMINA_LOW"));
            retList.add(map.get("MAIN_LOW"));
            retList.add(map.get("SECONDARY_LOW"));
        }
        else if(amulet){
            retList.add(map.get("STAMINA_LOW"));
            retList.add(map.get("SECONDARY_AMULET"));
        }
        else if(trinket){
            retList.add(map.get("STAMINA_TRINKET"));
            retList.add(map.get("MAIN_TRINKET"));
            retList.add(map.get("SECONDARY_TRINKET"));
        }
        else if(ohWeapon){
            retList.add(map.get("STAMINA_OH"));
            retList.add(map.get("MAIN_OH"));
            retList.add(map.get("SECONDARY_OH"));
        }
        else if(ohIntWeapon){
            retList.add(map.get("STAMINA_OH"));
            retList.add(map.get("MAIN_INT_OH"));
            retList.add(map.get("SECONDARY_OH"));
        }
        else if(thIntWeapon){
            retList.add(map.get("STAMINA_HIGH"));
            retList.add(map.get("MAIN_INT_TH"));
            retList.add(map.get("SECONDARY_HIGH"));
        }
        else if(offHandInt){
            retList.add(map.get("STAMINA_OH"));
            retList.add(map.get("MAIN_INT_OFF"));
            retList.add(map.get("SECONDARY_OH"));
        }
        else if(shield){
            retList.add(map.get("STAMINA_OH"));
            retList.add(map.get("MAIN_OH"));
            retList.add(map.get("MAIN_INT_OFF"));
            retList.add(map.get("SECONDARY_OH"));
        }
        return retList;
    }

}
