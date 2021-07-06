package pl.bloniarz.bis.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.bloniarz.bis.config.security.jwt.JwtUtil;
import pl.bloniarz.bis.model.dto.response.UsersCharactersResponse;
import pl.bloniarz.bis.model.dto.character.Character;
import pl.bloniarz.bis.model.dto.response.SimpleMessageResponse;
import pl.bloniarz.bis.service.CharacterService;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/characters")
public class CharacterController {

    private final CharacterService characterService;
    private final JwtUtil jwtUtil;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public UsersCharactersResponse getAllCharactersForActiveUser(HttpServletRequest request){
        return characterService.getAllCharactersForUser(
                jwtUtil.extractNameFromCookies(request.getCookies()));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{username}")
    public UsersCharactersResponse getAllCharactersForUsername(@PathVariable String username){
        return characterService.getAllCharactersForUser(username);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SimpleMessageResponse addCharacter(@RequestBody Character character, HttpServletRequest request){
        String activeUser = characterService.addCharacter(character,
                jwtUtil.extractNameFromCookies(request.getCookies()));
        return new SimpleMessageResponse(character.getName() + " added to user: " + activeUser);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public SimpleMessageResponse deleteCharacter(@PathVariable long id, HttpServletRequest request){
        String characterName = characterService.deleteCharacter(id,
                jwtUtil.extractNameFromCookies(request.getCookies()));
        return new SimpleMessageResponse(characterName + " deleted");
    }
}
