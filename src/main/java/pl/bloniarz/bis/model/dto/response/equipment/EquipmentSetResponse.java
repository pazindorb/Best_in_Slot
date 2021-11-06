package pl.bloniarz.bis.model.dto.response.equipment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.bloniarz.bis.model.dto.response.item.ItemResponse;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentSetResponse {

    private long id;
    private String characterName;
    private String name;
    private String specialization;
    private List<ItemResponse> itemList;

}
