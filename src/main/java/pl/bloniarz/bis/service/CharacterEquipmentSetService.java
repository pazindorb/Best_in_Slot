package pl.bloniarz.bis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bloniarz.bis.model.dao.character.CharacterEntity;
import pl.bloniarz.bis.model.dao.equipmentset.CharacterEquipmentSetEntity;
import pl.bloniarz.bis.model.dto.exceptions.AppErrorMessage;
import pl.bloniarz.bis.model.dto.exceptions.AppException;
import pl.bloniarz.bis.model.dto.request.equipmentset.CharacterEquipmentSetRequest;
import pl.bloniarz.bis.repository.CharacterEquipmentSetRepository;
import pl.bloniarz.bis.repository.CharacterRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CharacterEquipmentSetService {

    private final CharacterRepository characterRepository;
    private final CharacterEquipmentSetRepository setRepository;

    public void addEquipmentSet(String character, String actualUser, CharacterEquipmentSetRequest set) {
        CharacterEntity characterEntity = characterRepository.findOneByUsernameAndCharacterName(actualUser, character)
                .orElseThrow(() -> new AppException(AppErrorMessage.CHARACTER_NOT_FOUND, character));

        List<CharacterEquipmentSetEntity> list = characterEntity.getCharacterEquipmentSets();
        if(list == null)
            list = new ArrayList<>();

        CharacterEquipmentSetEntity setEntity = CharacterEquipmentSetEntity.builder()
                .name(set.getName())
                .specialization(set.getSpecialization())
                .character(characterEntity)
                .build();

        list.add(setEntity);

        setRepository.save(setEntity);
    }

}
