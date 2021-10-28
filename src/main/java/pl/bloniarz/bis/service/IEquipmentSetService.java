package pl.bloniarz.bis.service;

import org.springframework.transaction.annotation.Transactional;
import pl.bloniarz.bis.model.dto.request.equipment.EquipmentRequest;
import pl.bloniarz.bis.model.dto.request.equipment.ItemSetRequest;
import pl.bloniarz.bis.model.dto.response.equipment.EquipmentSetResponse;

import java.util.List;

public interface IEquipmentSetService {
    EquipmentSetResponse addEquipmentSet(String character, String activeUser, EquipmentRequest set);

    void deleteEquipmentSet(String character, long id, String activeUser);

    EquipmentSetResponse changeItemsInSet(long id, String character, String activeUser, List<ItemSetRequest> items);

    EquipmentSetResponse getAllItemsFromSet(long id, String character);
}
