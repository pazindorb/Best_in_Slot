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
import pl.bloniarz.bis.model.dto.response.item.ItemResponse;
import pl.bloniarz.bis.model.dto.response.equipment.EquipmentSetResponse;
import pl.bloniarz.bis.repository.EquipmentSetRepository;
import pl.bloniarz.bis.repository.CharacterRepository;
import pl.bloniarz.bis.repository.ItemRepository;
import pl.bloniarz.bis.repository.ItemSetRepository;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EquipmentSetService {

    private final CharacterRepository characterRepository;
    private final EquipmentSetRepository setRepository;
    private final ItemRepository itemRepository;
    private final ItemSetRepository itemSetRepository;
    private final ServicesUtil servicesUtil;

    public EquipmentSetResponse addEquipmentSet(String character, String activeUser, EquipmentRequest set) {
        CharacterEntity characterEntity = servicesUtil.getCharacterEntityFromNameAndActiveUser(character, activeUser);
        EquipmentEntity setEntity = EquipmentEntity.builder()
                .name(set.getName())
                .specialization(set.getSpecialization())
                .character(characterEntity)
                .build();
        setEntity = setRepository.save(setEntity);
        return parseSetEntityToEquipmentSetResponse(setEntity, character);
    }

    public void deleteEquipmentSet(String character, long id, String activeUser) {
        CharacterEntity characterEntity = servicesUtil.getCharacterEntityFromNameAndActiveUser(character, activeUser);
        EquipmentEntity setEntity = servicesUtil.getSetEntityFromCharacterEntityAndSetId(id, characterEntity);
        setRepository.delete(setEntity);
    }

    @Transactional
    public EquipmentSetResponse changeItemsInSet(long id, String character, String activeUser, List<ItemSetRequest> items) {
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
        return parseSetEntityToEquipmentSetResponse(setEntity, character);
    }

    @Transactional
    public EquipmentSetResponse getAllItemsFromSet(long id, String character, String activeUser) throws InvocationTargetException, IllegalAccessException {
        CharacterEntity characterEntity = characterRepository.findByUsernameAndCharacterName(activeUser, character)
                .orElse(characterRepository.findByName(character)
                        .orElseThrow(() -> new AppException(AppErrorMessage.CHARACTER_NOT_FOUND)));

        EquipmentEntity setEntity = servicesUtil.getSetEntityFromCharacterEntityAndSetId(id, characterEntity);

        return parseSetEntityToEquipmentSetResponse(setEntity, character);
    }

    private EquipmentSetResponse parseSetEntityToEquipmentSetResponse(EquipmentEntity setEntity, String character){
        List<ItemResponse> itemResponseList = new LinkedList<>();

        itemResponseList.add(parseItemSetEntityToItem(setEntity.getHead(), "head"));
        itemResponseList.add(parseItemSetEntityToItem(setEntity.getNeck(), "neck"));
        itemResponseList.add(parseItemSetEntityToItem(setEntity.getShoulders(), "shoulders"));
        itemResponseList.add(parseItemSetEntityToItem(setEntity.getChest(), "chest"));
        itemResponseList.add(parseItemSetEntityToItem(setEntity.getBack(), "back"));
        itemResponseList.add(parseItemSetEntityToItem(setEntity.getWrists(), "wrists"));
        itemResponseList.add(parseItemSetEntityToItem(setEntity.getHands(), "hands"));
        itemResponseList.add(parseItemSetEntityToItem(setEntity.getWaist(), "waist"));
        itemResponseList.add(parseItemSetEntityToItem(setEntity.getLegs(), "legs"));
        itemResponseList.add(parseItemSetEntityToItem(setEntity.getFeet(), "feet"));
        itemResponseList.add(parseItemSetEntityToItem(setEntity.getFirstRing(), "firstRing"));
        itemResponseList.add(parseItemSetEntityToItem(setEntity.getSecondRing(), "secondRing"));
        itemResponseList.add(parseItemSetEntityToItem(setEntity.getFirstTrinket(), "firstTrinket"));
        itemResponseList.add(parseItemSetEntityToItem(setEntity.getSecondTrinket(), "secondTrinket"));
        itemResponseList.add(parseItemSetEntityToItem(setEntity.getMainHand(), "mainHand"));
        itemResponseList.add(parseItemSetEntityToItem(setEntity.getOffHand(), "offHand"));

        return EquipmentSetResponse.builder()
                .id(setEntity.getId())
                .characterName(character)
                .name(setEntity.getName())
                .specialization(setEntity.getSpecialization())
                .itemList(itemResponseList)
                .build();
    }

    private ItemResponse parseItemSetEntityToItem(ItemSetEntity entity, String slotName) {
        if(entity == null) return ItemResponse.builder().slot(slotName).build();
        return servicesUtil.parseItemEntityToItem(entity.getItem(),entity.getItemLevel(),entity.getSocket(),slotName);
    }

}
