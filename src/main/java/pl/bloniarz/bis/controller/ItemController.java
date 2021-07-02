package pl.bloniarz.bis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.bloniarz.bis.model.dao.item.enums.ItemSlots;
import pl.bloniarz.bis.model.dto.response.ItemResponse;
import pl.bloniarz.bis.model.dto.response.SimpleMessageResponse;
import pl.bloniarz.bis.service.ItemService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/adminonly")
    public String helloAdmin(){
        return "HELLO ADMIN!";
    }

    @GetMapping("/user")
    public String hello(){
        return "HELLO USER!";
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ItemResponse> getItemsForSlot(@RequestParam String slot, @RequestParam int ilvl){
        return itemService.getItemsForSlot(ItemSlots.valueOf(slot), ilvl);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping
    public SimpleMessageResponse setAllItemsFromDropInstanceToOld(@RequestParam String dropZone){
        itemService.setAllItemsFromDropInstanceToOld(dropZone);
        return new SimpleMessageResponse("All items from " + dropZone + " changed to old.");
    }

}
