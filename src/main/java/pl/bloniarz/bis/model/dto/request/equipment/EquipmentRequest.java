package pl.bloniarz.bis.model.dto.request.equipment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentRequest {

    private String name;
    private String specialization;

}
