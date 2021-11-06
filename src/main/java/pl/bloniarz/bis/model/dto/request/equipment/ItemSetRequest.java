package pl.bloniarz.bis.model.dto.request.equipment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemSetRequest {

    private String slot;
    private long itemId;
    private int itemLevel;
    private boolean socket;
}
