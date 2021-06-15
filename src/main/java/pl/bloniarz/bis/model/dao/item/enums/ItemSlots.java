package pl.bloniarz.bis.model.dao.item.enums;

import lombok.AllArgsConstructor;
import pl.bloniarz.bis.externalapi.model.WowheadItemResponse;

@AllArgsConstructor
public enum ItemSlots {
    UNDEFINED(""),

    HEAD("Head"),
    NECK("Neck"),
    SHOULDERS("Shoulder"),
    CHEST("Chest"),
    BACK("Back"),
    WAIST("Waist"),
    LEGS("Legs"),
    FEET("Feet"),
    WRISTS("Wrist"),
    HANDS("Hands"),
    FINGER("Finger"),
    TRINKET("Trinket"),
    SHIELD("Off Hand"),
    OFF_HAND_ITEM("Held In Off-hand"),

    OH_WEAPON("One-Hand"),
    TH_WEAPON("Two-Hand"),
    RANGED("Ranged"),
    RANGED_INT("Ranged Int"),
    OH_INT_WEAPON("One-Hand Int"),
    TH_INT_WEAPON("Two-Hand Int"),

    ;
    final String name;

    public static ItemSlots extractSlotFromWowheadItem(WowheadItemResponse item){
        if(item.getCategory().equals("Weapons")){
            if(item.getStats().getIntelligence() != 0){
                String slot = item.getSlot() + " Int";
                return ItemSlots.fromName(slot);
            }
            else{
                return ItemSlots.fromName(item.getSlot());
            }
        }
        else {
            return ItemSlots.fromName(item.getSlot());
        }
    }

    public static ItemSlots fromName(String name){
        ItemSlots[] allValues = ItemSlots.values();

        for (ItemSlots allValue : allValues) {
            if (allValue.name.equals(name))
                return allValue;
        }
        return UNDEFINED;
    }

}
