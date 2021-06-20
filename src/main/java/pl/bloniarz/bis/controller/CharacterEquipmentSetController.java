package pl.bloniarz.bis.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.bloniarz.bis.config.security.jwt.JwtUtil;
import pl.bloniarz.bis.model.dto.request.equipmentset.CharacterEquipmentSetRequest;
import pl.bloniarz.bis.model.dto.response.SimpleMessageResponse;
import pl.bloniarz.bis.service.CharacterEquipmentSetService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{character}")
public class CharacterEquipmentSetController {

    private final JwtUtil jwtUtil;
    private final CharacterEquipmentSetService characterEquipmentSetService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SimpleMessageResponse addEquipmentSet(@PathVariable String character, @RequestBody CharacterEquipmentSetRequest set, HttpServletRequest request){
        String actualUser = jwtUtil.extractUserName(jwtUtil.extractTokenFromCookies(request.getCookies()));
        characterEquipmentSetService.addEquipmentSet(character, actualUser, set);
        return new SimpleMessageResponse("Set " + set.getName() + " added to: " + character);
    }

}
