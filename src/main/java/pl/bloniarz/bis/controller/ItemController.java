package pl.bloniarz.bis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.bloniarz.bis.externalapi.WowheadHttpClient;
import pl.bloniarz.bis.externalapi.model.LootSource;
import pl.bloniarz.bis.model.dao.item.enums.ItemSlots;
import pl.bloniarz.bis.model.dto.response.item.*;
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
    public ItemsForSlotResponse getItemsForSlot(@RequestParam String slot, @RequestParam int ilvl){
        return itemService.getItemsForSlot(ItemSlots.valueOf(slot), ilvl);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping
    public OldItemCollectionResponse setAllItemsFromDropInstanceToOld(@RequestParam String dropZone){
        return itemService.setAllItemsFromDropInstanceToOld(dropZone);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public OldItemResponse setItemWithSpecificIdToOld(@PathVariable long id){
        return itemService.setItemWithSpecificIdToOld(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AddedItemsResponse addItemsFromWowheadToDatabase(@RequestBody LootSource lootSource){
        return itemService.addAllItemsToDatabase(wowheadHttpClient.getItemsListForLootSource(lootSource), lootSource.getSourceName());
    }

}
