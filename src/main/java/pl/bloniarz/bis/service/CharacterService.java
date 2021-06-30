package pl.bloniarz.bis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bloniarz.bis.config.security.jwt.JwtUtil;
import pl.bloniarz.bis.model.dao.character.CharacterClasses;
import pl.bloniarz.bis.model.dao.character.CharacterEntity;
import pl.bloniarz.bis.model.dao.user.UserEntity;
import pl.bloniarz.bis.model.dto.exceptions.AppErrorMessage;
import pl.bloniarz.bis.model.dto.exceptions.AppException;
import pl.bloniarz.bis.model.dto.response.AllUsersCharactersResponse;
import pl.bloniarz.bis.model.dto.Character;
import pl.bloniarz.bis.repository.CharacterRepository;
import pl.bloniarz.bis.repository.UserRepository;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AllUsersCharactersResponse getAllCharactersForUser(Cookie[] cookies) {
        String activeUser = jwtUtil.extractUserName(jwtUtil.extractTokenFromCookies(cookies));
        return new AllUsersCharactersResponse(characterRepository.findAllCharactersOfUserNamed(activeUser).stream()
                .map(character -> Character.builder()
                        .name(character.getName())
                        .characterClass(character.getCharacterClass().toString())
                        .numberOfSets(character.getCharacterEquipmentSets().size())
                        .build())
                .collect(Collectors.toList()));

    }

    public String addCharacter(Character character, Cookie[] cookies) {
        String activeUser = jwtUtil.extractNameFromCookies(cookies);
        UserEntity userEntity = userRepository.findByLogin(activeUser)
                .orElseThrow(() -> new AppException(AppErrorMessage.USER_NOT_FOUND, activeUser));
        try{
            characterRepository.save(CharacterEntity.builder()
                    .user(userEntity)
                    .name(character.getName())
                    .characterClass(CharacterClasses.valueOf(character.getCharacterClass()))
                    .characterEquipmentSets(new ArrayList<>())
                    .build());
        }
        catch (Exception e){
            throw new AppException(AppErrorMessage.CHARACTER_ALREADY_EXISTS);
        }

        return activeUser;
    }

    public String deleteCharacter(long id, Cookie[] cookies) {
        String activeUser = jwtUtil.extractNameFromCookies(cookies);

        CharacterEntity characterEntity = characterRepository.findById(id)
                .filter(character -> character.getUser().getLogin().equals(activeUser))
                .orElseThrow(() -> new AppException(AppErrorMessage.NOT_OWNER));
        characterRepository.delete(characterEntity);
        return characterEntity.getName();
    }

}
