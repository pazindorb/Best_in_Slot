package pl.bloniarz.bis.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.bloniarz.bis.config.security.JwtUtil;
import pl.bloniarz.bis.model.dto.request.equipment.EquipmentRequest;
import pl.bloniarz.bis.model.dto.request.equipment.ItemSetRequest;
import pl.bloniarz.bis.model.dto.response.equipment.EquipmentSetResponse;
import pl.bloniarz.bis.service.EquipmentSetService;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{character}")
public class EquipmentSetController {

    private final EquipmentSetService equipmentSetService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EquipmentSetResponse addEquipmentSet(@PathVariable String character, @RequestBody EquipmentRequest set, Principal principal){
        return equipmentSetService.addEquipmentSet(character, principal.getName(), set);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteEquipmentSet(@PathVariable String character, @PathVariable long id, Principal principal){
        equipmentSetService.deleteEquipmentSet(character,id, principal.getName());
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public EquipmentSetResponse changeItemsInSet(@PathVariable String character, @PathVariable long id, @RequestBody List<ItemSetRequest> set, Principal principal){
        return equipmentSetService.changeItemsInSet(id, character, principal.getName(), set);
    }

    @GetMapping("/{id}")
    public EquipmentSetResponse getAllItemsFromSet(@PathVariable String character, @PathVariable long id) {
        return equipmentSetService.getAllItemsFromSet(id,character);
    }

}
