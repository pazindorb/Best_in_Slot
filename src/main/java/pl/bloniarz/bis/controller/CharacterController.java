package pl.bloniarz.bis.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.bloniarz.bis.model.dto.response.AllUsersCharactersResponse;
import pl.bloniarz.bis.model.dto.Character;
import pl.bloniarz.bis.model.dto.response.SimpleMessageResponse;
import pl.bloniarz.bis.service.CharacterService;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/characters")
public class CharacterController {

    private final CharacterService characterService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public AllUsersCharactersResponse getAllCharactersForUser(HttpServletRequest request){
        return characterService.getAllCharactersForUser(request.getCookies());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public SimpleMessageResponse addCharacter(@RequestBody Character character, HttpServletRequest request){
        String activeUser = characterService.addCharacter(character, request.getCookies());
        return new SimpleMessageResponse(character.getName() + " added to user: " + activeUser);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public SimpleMessageResponse deleteCharacter(@PathVariable long id, HttpServletRequest request){
        String characterName = characterService.deleteCharacter(id, request.getCookies());
        return new SimpleMessageResponse(characterName + " deleted");
    }


}
