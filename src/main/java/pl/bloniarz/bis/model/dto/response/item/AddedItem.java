package pl.bloniarz.bis.model.dto.response.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.bloniarz.bis.model.dao.item.enums.ItemTypes;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddedItem {

    private long id;
    private String slot;
    private String name;
    private String wowheadLink;
    private ItemTypes type;

}
