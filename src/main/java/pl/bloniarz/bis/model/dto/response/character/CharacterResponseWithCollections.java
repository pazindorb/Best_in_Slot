package pl.bloniarz.bis.model.dto.response.character;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.bloniarz.bis.model.dto.response.equipment.EquipmentSetResponse;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CharacterResponseWithCollections {
    private long id;
    private String name;
    private String characterClass;

    private List<EquipmentSetResponse> equipment;
}
