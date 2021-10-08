package pl.bloniarz.bis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bloniarz.bis.model.dao.character.CharacterClasses;
import pl.bloniarz.bis.model.dao.character.CharacterEntity;
import pl.bloniarz.bis.model.dao.user.UserEntity;
import pl.bloniarz.bis.model.dto.exceptions.AppErrorMessage;
import pl.bloniarz.bis.model.dto.exceptions.AppException;
import pl.bloniarz.bis.model.dto.request.character.CharacterRequest;
import pl.bloniarz.bis.model.dto.response.character.UsersCharactersResponse;
import pl.bloniarz.bis.model.dto.response.character.CharacterResponse;
import pl.bloniarz.bis.repository.CharacterRepository;
import pl.bloniarz.bis.repository.UserRepository;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final UserRepository userRepository;

    public UsersCharactersResponse getAllCharactersForUser(String username) {
        return getUsersCharactersFromUsername(username);
    }

    public CharacterResponse addCharacter(CharacterRequest characterRequest, String username) {
        CharacterEntity characterEntity;
        UserEntity userEntity = userRepository.findByLogin(username)
                .orElseThrow(() -> new AppException(AppErrorMessage.USER_NOT_FOUND, username));
        try{
            characterEntity = characterRepository.save(CharacterEntity.builder()
                    .user(userEntity)
                    .name(characterRequest.getName())
                    .characterClass(CharacterClasses.valueOf(characterRequest.getCharacterClass()))
                    .characterEquipmentSets(new ArrayList<>())
                    .build());
        }
        catch (Exception e){
            throw new AppException(AppErrorMessage.CHARACTER_ALREADY_EXISTS);
        }
        return parseCharacterEntityToCharacter(characterEntity);
    }

    public String deleteCharacter(long id, String username) {
        CharacterEntity characterEntity = characterRepository.findById(id)
                .filter(character -> character.getUser().getLogin().equals(username))
                .orElseThrow(() -> new AppException(AppErrorMessage.NOT_OWNER));
        characterRepository.delete(characterEntity);
        return characterEntity.getName();
    }

    private UsersCharactersResponse getUsersCharactersFromUsername(String username) {
        return new UsersCharactersResponse(username,
                characterRepository.findAllCharactersOfUserNamed(username).stream()
                .map(this::parseCharacterEntityToCharacter)
                .collect(Collectors.toList()));
    }

    private CharacterResponse parseCharacterEntityToCharacter(CharacterEntity characterEntity){
        return CharacterResponse.builder()
                .id(characterEntity.getId())
                .name(characterEntity.getName())
                .characterClass(characterEntity.getCharacterClass().toString())
                .numberOfSets(characterEntity.getCharacterEquipmentSets().size())
                .build();
    }
}
