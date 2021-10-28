package pl.bloniarz.bis.service;

import org.springframework.transaction.annotation.Transactional;
import pl.bloniarz.bis.model.dto.request.character.CharacterRequest;
import pl.bloniarz.bis.model.dto.response.character.CharacterResponse;
import pl.bloniarz.bis.model.dto.response.character.CharacterResponseWithCollections;
import pl.bloniarz.bis.model.dto.response.character.UsersCharactersResponse;

public interface ICharacterService {
    UsersCharactersResponse getAllCharactersForUser(String username);

    UsersCharactersResponse getAllCharactersForUser(long id);

    CharacterResponse addCharacter(CharacterRequest characterRequest, String username);

    void deleteCharacter(long id, String username);

    CharacterResponseWithCollections getCharacterWithEquipmentSets(long id);

    CharacterResponse editCharacter(CharacterRequest characterRequest, String username, long id);
}
