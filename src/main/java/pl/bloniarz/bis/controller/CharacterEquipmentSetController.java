package pl.bloniarz.bis.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.bloniarz.bis.config.security.jwt.JwtUtil;
import pl.bloniarz.bis.model.dto.request.equipmentset.CharacterEquipmentSetRequest;
import pl.bloniarz.bis.model.dto.request.equipmentset.ItemSetRequest;
import pl.bloniarz.bis.model.dto.response.ItemSetResponse;
import pl.bloniarz.bis.model.dto.response.SimpleMessageResponse;
import pl.bloniarz.bis.service.CharacterEquipmentSetService;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{character}")
public class CharacterEquipmentSetController {

    private final JwtUtil jwtUtil;
    private final CharacterEquipmentSetService characterEquipmentSetService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SimpleMessageResponse addEquipmentSet(@PathVariable String character, @RequestBody CharacterEquipmentSetRequest set, HttpServletRequest request){
        String activeUser = jwtUtil.extractNameFromCookies(request.getCookies());
        characterEquipmentSetService.addEquipmentSet(character, activeUser, set);
        return new SimpleMessageResponse("Set " + set.getName() + " added to: " + character);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public SimpleMessageResponse deleteEquipmentSet(@PathVariable String character, @PathVariable long id, HttpServletRequest request){
        String activeUser = jwtUtil.extractNameFromCookies(request.getCookies());
        characterEquipmentSetService.deleteEquipmentSet(character,id,activeUser);
        return new SimpleMessageResponse("Set deleted");
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public SimpleMessageResponse changeItemsInSet(@PathVariable String character, @PathVariable long id, @RequestBody List<ItemSetRequest> set, HttpServletRequest request){
        String activeUser = jwtUtil.extractNameFromCookies(request.getCookies());
        characterEquipmentSetService.changeItemsInSet(id, character, activeUser, set);
        return new SimpleMessageResponse("Done");
    }

    @GetMapping("/{id}")
    public ItemSetResponse getAllItemsFromSet(@PathVariable String character, @PathVariable long id, HttpServletRequest request){
        String activeUser = jwtUtil.extractNameFromCookies(request.getCookies());
        try {
            return characterEquipmentSetService.getAllItemsFromSet(id,character,activeUser);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
