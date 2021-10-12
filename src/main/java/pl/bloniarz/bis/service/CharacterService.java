package pl.bloniarz.bis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bloniarz.bis.model.dao.character.CharacterClasses;
import pl.bloniarz.bis.model.dao.character.CharacterEntity;
import pl.bloniarz.bis.model.dao.user.UserEntity;
import pl.bloniarz.bis.model.dto.exceptions.AppErrorMessage;
import pl.bloniarz.bis.model.dto.exceptions.AppException;
import pl.bloniarz.bis.model.dto.request.character.CharacterRequest;
import pl.bloniarz.bis.model.dto.response.character.CharacterResponseWithCollections;
import pl.bloniarz.bis.model.dto.response.character.UsersCharactersResponse;
import pl.bloniarz.bis.model.dto.response.character.CharacterResponse;
import pl.bloniarz.bis.repository.CharacterRepository;
import pl.bloniarz.bis.repository.EquipmentSetRepository;
import pl.bloniarz.bis.repository.UserRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final ServicesUtil servicesUtil;
    private final CharacterRepository characterRepository;
    private final UserRepository userRepository;
    private final EquipmentSetRepository equipmentSetRepository;

    public UsersCharactersResponse getAllCharactersForUser(String username) {
        return getUsersCharactersFromUsername(username);
    }
    public UsersCharactersResponse getAllCharactersForUser(long id) {
        return getUsersCharactersFromUserId(id);
    }

    public CharacterResponse addCharacter(CharacterRequest characterRequest, String username) {
        UserEntity userEntity = userRepository.findByLoginAndActiveIsTrue(username)
                .orElseThrow(() -> new AppException(AppErrorMessage.USER_NOT_FOUND, username));

        CharacterEntity characterEntity = characterRepository.save(CharacterEntity.builder()
                .user(userEntity)
                .name(characterRequest.getName())
                .characterClass(CharacterClasses.valueOf(characterRequest.getCharacterClass()))
                .characterEquipmentSets(new ArrayList<>())
                .active(true)
                .build());

        return parseCharacterEntityToCharacterResponse(characterEntity);
    }

    @Transactional
    public void deleteCharacter(long id, String username) {
        CharacterEntity characterEntity = characterRepository.findByIdAndActiveIsTrue(id)
                .filter(character -> character.getUser().getLogin().equals(username))
                .orElseThrow(() -> new AppException(AppErrorMessage.NOT_OWNER));
        characterEntity.delete();
        characterRepository.save(characterEntity);
    }

    @Transactional
    public CharacterResponseWithCollections getCharacterWithEquipmentSets(long id) {
        CharacterEntity characterEntity = characterRepository.findByIdAndActiveIsTrue(id)
                .orElseThrow(() -> new AppException(AppErrorMessage.CHARACTER_NOT_FOUND_ID));

        return parseCharacterEntityToDetailedCharacter(characterEntity);
    }

    public CharacterResponse editCharacter(CharacterRequest characterRequest, String username, long id) {
        CharacterEntity characterEntity = characterRepository.findByIdAndActiveIsTrue(id)
                .filter(character -> character.getUser().getLogin().equals(username))
                .orElseThrow(() -> new AppException(AppErrorMessage.NOT_OWNER));

        if(!(characterRequest.getCharacterClass()==null || characterRequest.getCharacterClass().equals("")))
            characterEntity.setCharacterClass(CharacterClasses.valueOf(characterRequest.getCharacterClass()));

        if(!(characterRequest.getName()==null || characterRequest.getName().equals("")))
            characterEntity.setName(characterRequest.getName());

        return parseCharacterEntityToCharacterResponse(characterRepository.save(characterEntity));
    }

    private UsersCharactersResponse getUsersCharactersFromUsername(String username) {
        return UsersCharactersResponse.builder()
                .characters(characterRepository.findAllCharactersOfUserNamed(username).stream()
                        .map(this::parseCharacterEntityToCharacterResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    private UsersCharactersResponse getUsersCharactersFromUserId(long id) {
        return UsersCharactersResponse.builder()
                .characters(characterRepository.findAllCharactersOfUserId(id).stream()
                        .map(this::parseCharacterEntityToCharacterResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    private CharacterResponseWithCollections parseCharacterEntityToDetailedCharacter(CharacterEntity characterEntity){
        return CharacterResponseWithCollections.builder()
                .id(characterEntity.getId())
                .name(characterEntity.getName())
                .characterClass(characterEntity.getCharacterClass().toString())
                .equipment(characterEntity.getCharacterEquipmentSets().stream()
                        .map(entity -> servicesUtil.parseSetEntityToEquipmentSetResponse(entity, characterEntity.getName(), false))
                        .collect(Collectors.toList()))
                .build();
    }

    private CharacterResponse parseCharacterEntityToCharacterResponse(CharacterEntity characterEntity){
        return CharacterResponse.builder()
                .id(characterEntity.getId())
                .name(characterEntity.getName())
                .characterClass(characterEntity.getCharacterClass().toString())
                .numberOfSets(equipmentSetRepository.getCountOfSetForCharacterId(characterEntity.getId()))
                .build();
    }


}
