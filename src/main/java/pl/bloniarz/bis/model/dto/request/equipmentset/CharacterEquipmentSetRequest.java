package pl.bloniarz.bis.model.dto.request.equipmentset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterEquipmentSetRequest {

    private String name;
    private String specialization;

}
