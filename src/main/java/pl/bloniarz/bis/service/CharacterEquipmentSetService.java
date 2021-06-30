package pl.bloniarz.bis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bloniarz.bis.model.dao.character.CharacterEntity;
import pl.bloniarz.bis.model.dao.equipmentset.CharacterEquipmentSetEntity;
import pl.bloniarz.bis.model.dao.equipmentset.ItemSetEntity;
import pl.bloniarz.bis.model.dto.exceptions.AppErrorMessage;
import pl.bloniarz.bis.model.dto.exceptions.AppException;
import pl.bloniarz.bis.model.dto.request.equipmentset.CharacterEquipmentSetRequest;
import pl.bloniarz.bis.model.dto.request.equipmentset.ItemSetRequest;
import pl.bloniarz.bis.repository.CharacterEquipmentSetRepository;
import pl.bloniarz.bis.repository.CharacterRepository;
import pl.bloniarz.bis.repository.ItemRepository;
import pl.bloniarz.bis.repository.ItemSetRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CharacterEquipmentSetService {

    private final CharacterRepository characterRepository;
    private final CharacterEquipmentSetRepository setRepository;
    private final ItemRepository itemRepository;
    private final ItemSetRepository itemSetRepository;

    public void addEquipmentSet(String character, String activeUser, CharacterEquipmentSetRequest set) {
        CharacterEntity characterEntity = characterRepository.findOneByUsernameAndCharacterName(activeUser, character)
                .orElseThrow(() -> new AppException(AppErrorMessage.CHARACTER_NOT_FOUND, character));
        CharacterEquipmentSetEntity setEntity = CharacterEquipmentSetEntity.builder()
                .name(set.getName())
                .specialization(set.getSpecialization())
                .character(characterEntity)
                .build();
        setRepository.save(setEntity);
    }

    public void deleteEquipmentSet(String character, long id, String activeUser) {
        CharacterEntity characterEntity = characterRepository.findOneByUsernameAndCharacterName(activeUser, character)
                .orElseThrow(() -> new AppException(AppErrorMessage.CHARACTER_NOT_FOUND, character));
        CharacterEquipmentSetEntity setEntity = setRepository.findByIdAndCharacter(id, characterEntity)
                .orElseThrow(() -> new AppException(AppErrorMessage.SET_NOT_FOUND));
        setRepository.delete(setEntity);
    }

    @Transactional
    public void changeItemsInSet(long id, String character, String activeUser, List<ItemSetRequest> items) {
        CharacterEntity characterEntity = characterRepository.findOneByUsernameAndCharacterName(activeUser, character)
                .orElseThrow(() -> new AppException(AppErrorMessage.CHARACTER_NOT_FOUND, character));
        CharacterEquipmentSetEntity setEntity = setRepository.findByIdAndCharacter(id, characterEntity)
                .orElseThrow(() -> new AppException(AppErrorMessage.SET_NOT_FOUND));
        items.forEach(item -> {
            ItemSetEntity itemSet = ItemSetEntity.builder()
                    .item(itemRepository.getById(item.getItemId()))
                    .socket(false)
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
                    setEntity.setWrists(itemSet);
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
}
