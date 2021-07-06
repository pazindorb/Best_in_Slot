package pl.bloniarz.bis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bloniarz.bis.model.dao.character.CharacterEntity;
import pl.bloniarz.bis.model.dao.equipmentset.EquipmentEntity;
import pl.bloniarz.bis.model.dao.equipmentset.ItemSetEntity;
import pl.bloniarz.bis.model.dto.exceptions.AppErrorMessage;
import pl.bloniarz.bis.model.dto.exceptions.AppException;
import pl.bloniarz.bis.model.dto.request.equipment.EquipmentRequest;
import pl.bloniarz.bis.model.dto.request.equipment.ItemSetRequest;
import pl.bloniarz.bis.model.dto.response.ItemResponse;
import pl.bloniarz.bis.model.dto.response.ItemSetResponse;
import pl.bloniarz.bis.repository.CharacterEquipmentSetRepository;
import pl.bloniarz.bis.repository.CharacterRepository;
import pl.bloniarz.bis.repository.ItemRepository;
import pl.bloniarz.bis.repository.ItemSetRepository;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CharacterEquipmentSetService {

    private final CharacterRepository characterRepository;
    private final CharacterEquipmentSetRepository setRepository;
    private final ItemRepository itemRepository;
    private final ItemSetRepository itemSetRepository;
    private final ServicesUtil servicesUtil;

    public void addEquipmentSet(String character, String activeUser, EquipmentRequest set) {
        CharacterEntity characterEntity = servicesUtil.getCharacterEntityFromNameAndActiveUser(character, activeUser);
        EquipmentEntity setEntity = EquipmentEntity.builder()
                .name(set.getName())
                .specialization(set.getSpecialization())
                .character(characterEntity)
                .build();
        setRepository.save(setEntity);
    }

    public void deleteEquipmentSet(String character, long id, String activeUser) {
        CharacterEntity characterEntity = servicesUtil.getCharacterEntityFromNameAndActiveUser(character, activeUser);
        EquipmentEntity setEntity = servicesUtil.getSetEntityFromCharacterEntityAndSetId(id, characterEntity);
        setRepository.delete(setEntity);
    }

    @Transactional
    public void changeItemsInSet(long id, String character, String activeUser, List<ItemSetRequest> items) {
        CharacterEntity characterEntity = servicesUtil.getCharacterEntityFromNameAndActiveUser(character, activeUser);
        EquipmentEntity setEntity = servicesUtil.getSetEntityFromCharacterEntityAndSetId(id, characterEntity);
        items.forEach(item -> {
            ItemSetEntity itemSet = ItemSetEntity.builder()
                    .item(itemRepository.getById(item.getItemId()))
                    .socket(0)
                    .itemLevel(item.getItemLevel())
                    .build();

            itemSet = itemSetRepository.save(itemSet);
            switch (item.getSlot()){
                case "head":
                    setEntity.setHead(itemSet);
                    break;
                case "neck":
                    setEntity.setNeck(itemSet);
                    break;
                case "shoulders":
                    setEntity.setShoulders(itemSet);
                    break;
                case "chest":
                    setEntity.setChest(itemSet);
                    break;
                case "back":
                    setEntity.setBack(itemSet);
                    break;
                case "wrists":
                    setEntity.setWrists(itemSet);
                    break;
                case "hands":
                    setEntity.setHands(itemSet);
                    break;
                case "waist":
                    setEntity.setWaist(itemSet);
                    break;
                case "legs":
                    setEntity.setLegs(itemSet);
                    break;
                case "feet":
                    setEntity.setFeet(itemSet);
                    break;
                case "firstRing":
                    setEntity.setFirstRing(itemSet);
                    break;
                case "secondRing":
                    setEntity.setSecondRing(itemSet);
                    break;
                case "firstTrinket":
                    setEntity.setFirstTrinket(itemSet);
                    break;
                case "secondTrinket":
                    setEntity.setSecondTrinket(itemSet);
                    break;
                case "mainHand":
                    setEntity.setMainHand(itemSet);
                    break;
                case "offHand":
                    setEntity.setOffHand(itemSet);
                    break;
                default:
                    itemSetRepository.delete(itemSet);
                    throw new AppException(AppErrorMessage.SLOT_NOT_FOUND);
            }
        });
    }

    @Transactional
    public ItemSetResponse getAllItemsFromSet(long id, String character, String activeUser) throws InvocationTargetException, IllegalAccessException {
        CharacterEntity characterEntity = characterRepository.findByUsernameAndCharacterName(activeUser, character)
                .orElse(characterRepository.findByName(character)
                        .orElseThrow(() -> new AppException(AppErrorMessage.CHARACTER_NOT_FOUND)));

        EquipmentEntity setEntity = servicesUtil.getSetEntityFromCharacterEntityAndSetId(id, characterEntity);

        List<ItemResponse> itemResponses = new LinkedList<>();

        itemResponses.add(parseItemSetEntityToItem(setEntity.getHead(), "head"));
        itemResponses.add(parseItemSetEntityToItem(setEntity.getNeck(), "neck"));
        itemResponses.add(parseItemSetEntityToItem(setEntity.getShoulders(), "shoulders"));
        itemResponses.add(parseItemSetEntityToItem(setEntity.getChest(), "chest"));
        itemResponses.add(parseItemSetEntityToItem(setEntity.getBack(), "back"));
        itemResponses.add(parseItemSetEntityToItem(setEntity.getWrists(), "wrists"));
        itemResponses.add(parseItemSetEntityToItem(setEntity.getHands(), "hands"));
        itemResponses.add(parseItemSetEntityToItem(setEntity.getWaist(), "waist"));
        itemResponses.add(parseItemSetEntityToItem(setEntity.getLegs(), "legs"));
        itemResponses.add(parseItemSetEntityToItem(setEntity.getFeet(), "feet"));
        itemResponses.add(parseItemSetEntityToItem(setEntity.getFirstRing(), "firstRing"));
        itemResponses.add(parseItemSetEntityToItem(setEntity.getSecondRing(), "secondRing"));
        itemResponses.add(parseItemSetEntityToItem(setEntity.getFirstTrinket(), "firstTrinket"));
        itemResponses.add(parseItemSetEntityToItem(setEntity.getSecondTrinket(), "secondTrinket"));
        itemResponses.add(parseItemSetEntityToItem(setEntity.getMainHand(), "mainHand"));
        itemResponses.add(parseItemSetEntityToItem(setEntity.getOffHand(), "offHand"));

        return ItemSetResponse.builder()
                .characterName(character)
                .name(setEntity.getName())
                .specialization(setEntity.getSpecialization())
                .itemList(itemResponses)
                .build();
    }

    private ItemResponse parseItemSetEntityToItem(ItemSetEntity entity, String slotName) {
        if(entity == null) return ItemResponse.builder().slot(slotName).build();
        return servicesUtil.parseItemEntityToItem(entity.getItem(),entity.getItemLevel(),entity.getSocket(),slotName);
    }


}
