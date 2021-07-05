package pl.bloniarz.bis.model.dto.request.equipmentset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.bloniarz.bis.model.dao.item.ItemEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemSetRequest {

    private String slot;
    private long itemId;
    private int itemLevel;
    private boolean socket;
}
