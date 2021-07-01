package pl.bloniarz.bis.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.bloniarz.bis.model.dao.item.enums.ItemSlots;
import pl.bloniarz.bis.model.dao.item.enums.ItemTypes;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemResponse {

    private String slot;
    private String name;
    private int itemLevel;
    private int socket;

    private int stamina;
    private int strength;
    private int agility;
    private int intelligence;

    private int criticalStrike;
    private int mastery;
    private int haste;
    private int versatility;

    private String dropInstance;
    private String wowheadLink;
    private ItemTypes type;

}
