package pl.bloniarz.bis.service;

import org.springframework.transaction.annotation.Transactional;
import pl.bloniarz.bis.externalapi.model.WowheadItemResponse;
import pl.bloniarz.bis.model.dao.item.enums.ItemSlots;
import pl.bloniarz.bis.model.dto.response.item.AddedItemsResponse;
import pl.bloniarz.bis.model.dto.response.item.ItemsForSlotResponse;
import pl.bloniarz.bis.model.dto.response.item.OldItemCollectionResponse;
import pl.bloniarz.bis.model.dto.response.item.OldItemResponse;

import java.util.List;

public interface IItemService {
    AddedItemsResponse addAllItemsToDatabase(List<WowheadItemResponse> wowheadItemResponseList, String dropInstance);

    ItemsForSlotResponse getItemsForSlot(ItemSlots slot, int itemLevel);

    OldItemCollectionResponse setAllItemsFromDropInstanceToOld(String dropInstance);

    OldItemResponse setItemWithSpecificIdToOld(long id);
}
