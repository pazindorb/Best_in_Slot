package pl.bloniarz.bis.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.bloniarz.bis.config.security.JwtUtil;
import pl.bloniarz.bis.model.dto.request.character.CharacterRequest;
import pl.bloniarz.bis.model.dto.response.character.CharacterResponseWithCollections;
import pl.bloniarz.bis.model.dto.response.character.UsersCharactersResponse;
import pl.bloniarz.bis.model.dto.response.character.CharacterResponse;
import pl.bloniarz.bis.service.CharacterService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/characters")
public class CharacterController {

    private final CharacterService characterService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public UsersCharactersResponse getAllCharactersForActiveUser(Principal principal){
        return characterService.getAllCharactersForUser(principal.getName());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public UsersCharactersResponse getAllCharactersForUsername(@PathVariable long id){
        return characterService.getAllCharactersForUser(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/character/{id}")
    public CharacterResponseWithCollections getCharacterWithEquipmentSets(@PathVariable long id){
        return characterService.getCharacterWithEquipmentSets(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CharacterResponse addCharacter(@RequestBody CharacterRequest characterRequest, Principal principal){
        return characterService.addCharacter(characterRequest, principal.getName());
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public CharacterResponse editCharacter(@RequestBody CharacterRequest characterRequest, Principal principal, @PathVariable long id){
        return characterService.editCharacter(characterRequest, principal.getName(), id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCharacter(@PathVariable long id, Principal principal){
        characterService.deleteCharacter(id, principal.getName());
    }
}
