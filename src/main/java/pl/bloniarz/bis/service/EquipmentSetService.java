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

import java.util.*;

@Service
@RequiredArgsConstructor
public class EquipmentSetService {

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
                .active(true)
                .build();
        setEntity = setRepository.save(setEntity);
        return servicesUtil.parseSetEntityToEquipmentSetResponse(setEntity, character, true);
    }

    public void deleteEquipmentSet(String character, long id, String activeUser) {
        CharacterEntity characterEntity = servicesUtil.getCharacterEntityFromNameAndActiveUser(character, activeUser);
        EquipmentEntity setEntity = servicesUtil.getSetEntityFromCharacterEntityAndSetId(id, characterEntity);
        setEntity.delete();
        setRepository.save(setEntity);
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
        return servicesUtil.parseSetEntityToEquipmentSetResponse(setEntity, character, true);
    }

    @Transactional
    public EquipmentSetResponse getAllItemsFromSet(long id, String character) {
        EquipmentEntity setEntity = setRepository.findByIdAndCharacterName(id, character)
                .orElseThrow(() -> new AppException(AppErrorMessage.SET_NOT_FOUND));

        return servicesUtil.parseSetEntityToEquipmentSetResponse(setEntity, character, true);
    }

}
