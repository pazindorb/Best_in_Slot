package pl.bloniarz.bis.model.dto.request.equipmentset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterEquipmentSetRequest {

    private String name;
    private String specialization;

}
