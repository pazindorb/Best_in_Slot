package pl.bloniarz.bis.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.bloniarz.bis.config.security.JwtUtil;
import pl.bloniarz.bis.model.dto.request.character.CharacterRequest;
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
    private final JwtUtil jwtUtil;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public UsersCharactersResponse getAllCharactersForActiveUser(Principal principal){
        return characterService.getAllCharactersForUser(principal.getName());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{username}")
    public UsersCharactersResponse getAllCharactersForUsername(@PathVariable String username){
        return characterService.getAllCharactersForUser(username);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CharacterResponse addCharacter(@RequestBody CharacterRequest characterRequest, Principal principal){
        return characterService.addCharacter(characterRequest,
                principal.getName());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCharacter(@PathVariable long id, Principal principal){
        characterService.deleteCharacter(id, principal.getName());
    }
}
