package pl.bloniarz.bis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bloniarz.bis.externalapi.model.WowheadItemResponse;
import pl.bloniarz.bis.model.dao.item.ItemEntity;
import pl.bloniarz.bis.model.dao.item.ItemStatsEntity;
import pl.bloniarz.bis.model.dao.item.enums.ItemSlots;
import pl.bloniarz.bis.model.dao.item.enums.ItemStatTemplate;
import pl.bloniarz.bis.model.dao.item.enums.ItemTypes;
import pl.bloniarz.bis.repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public void addAllItemsToDatabase(List<WowheadItemResponse> wowheadItemResponseList){
        itemRepository.saveAll(parseWowheadItemResponseListToItemEntityList(wowheadItemResponseList));
    }

    public List<ItemEntity> parseWowheadItemResponseListToItemEntityList(List<WowheadItemResponse> wowheadItemResponseList) {
        List<ItemEntity> entities = wowheadItemResponseList.stream()
                .filter(item -> {
                    if (item.getQuality().equals("Rare") || item.getQuality().equals("Epic") || item.getQuality().equals("Legendary"))
                        return true;
                    return false;
                })
                .map(item -> {
                    List<ItemStatsEntity> statsEntityList = new ArrayList<>();
                    List<Integer> range = new ArrayList<>();

                    ItemTypes type = ItemTypes.fromName(item.getType());
                    ItemSlots slot = ItemSlots.extractSlotFromWowheadItem(item);
                    ItemEntity itemEntity = ItemEntity.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .type(type)
                            .slot(slot)
                            .dropInstance(item.getDropInstance())
                            .wowheadLink(item.getWowHeadLink())
                            .build();

                    if(item.getItemLevel() == 200)
                        range.add(226);
                    else if(item.getItemLevel() == 207)
                    {
                        range.add(233);
                    }
                    else if(item.getItemLevel() == 158)
                    {
                        range.add(226);
                        range.add(220);
                    }

                    range.forEach(ilvl -> {
                        ItemStatsEntity statsEntity = ItemStatsEntity.builder()
                                .old(false)
                                .haste(item.getStats().getHaste())
                                .mastery(item.getStats().getMastery())
                                .criticalStrike(item.getStats().getCriticalStrike())
                                .versatility(item.getStats().getVersatility())
                                .itemLevel(ilvl)
                                .build();

                        if(slot == ItemSlots.SHIELD){
                            statsEntity.setIntelligence(ItemStatTemplate.getTemplateForPieceAndIlvl(ItemSlots.OFF_HAND_ITEM,ilvl).getMainStat());
                            statsEntity.setStrength(ItemStatTemplate.getTemplateForPieceAndIlvl(ItemSlots.OH_WEAPON,ilvl).getMainStat());
                        }
                        else{
                            if(item.getStats().getIntelligence() != 0)
                                statsEntity.setIntelligence(ItemStatTemplate.getTemplateForPieceAndIlvl(slot,ilvl).getMainStat());
                            if(item.getStats().getStrength() != 0)
                                statsEntity.setStrength(ItemStatTemplate.getTemplateForPieceAndIlvl(slot,ilvl).getMainStat());
                            if(item.getStats().getAgility() != 0)
                                statsEntity.setAgility(ItemStatTemplate.getTemplateForPieceAndIlvl(slot,ilvl).getMainStat());
                        }
                        if(item.getStats().getStamina() != 0)
                            statsEntity.setStamina(ItemStatTemplate.getTemplateForPieceAndIlvl(slot,ilvl).getStamina());
                        if(item.getStats().getSecondary() != 0)
                            statsEntity.setSecondary(ItemStatTemplate.getTemplateForPieceAndIlvl(slot,ilvl).getSecondaryStat());

                        statsEntityList.add(statsEntity);
                    });
                    itemEntity.setStats(statsEntityList);
                    statsEntityList.forEach(element -> {
                        element.setItem(itemEntity);
                    });
                    return itemEntity;
                })
                .collect(Collectors.toList());
        return entities;
    }

}
