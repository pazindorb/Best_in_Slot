package pl.bloniarz.bis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.bloniarz.bis.externalapi.WowheadHttpClient;
import pl.bloniarz.bis.externalapi.model.LootSource;
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
    private final WowheadHttpClient wowheadHttpClient;

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

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public SimpleMessageResponse setItemWithSpecificIdToOld(@PathVariable long id){
        itemService.setItemWithSpecificIdToOld(id);
        return new SimpleMessageResponse("Items with id: " + id + " changed to old.");
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SimpleMessageResponse addItemsFromWowheadToDatabase(@RequestBody LootSource lootSource){
        itemService.addAllItemsToDatabase(wowheadHttpClient.getItemsListForLootSource(lootSource));
        return new SimpleMessageResponse(lootSource.getSourceName() + " : Added to database");
    }

}
