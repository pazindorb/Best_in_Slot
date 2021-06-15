package pl.bloniarz.bis.model.dao.item.enums;

import lombok.Getter;

@Getter
public enum ItemStatTemplate {
    UNDEFINED(0,0,0),

    HIGH_220(138,77,137),
    MEDIUM_220(104,58,103),
    LOW_220(78,43,77),
    AMULET_220(75,0,206),
    TRINKET_220(132,73,98),

    OH_WEAPON_220(69,39,69),
    TH_INT_WEAPON_220(138,342,137),
    OH_INT_WEAPON_220(69,224,69),
    OFF_INT_WEAPON_220(69,118,69),


    HIGH_226(149,81,142),
    MEDIUM_226(112,61,107),
    LOW_226(84,46,80),
    AMULET_226(84,0,216),
    TRINKET_226(142,77,101),

    OH_WEAPON_226(75,41,71),
    TH_INT_WEAPON_226(149,361,142),
    OH_INT_WEAPON_226(75,237,71),
    OFF_INT_WEAPON_226(75,125,71),


    HIGH_233(162,87,147),
    MEDIUM_233(122,65,110),
    LOW_233(91,49,83),
    AMULET_233(91,0,227),
    TRINKET_233(154,82,105),

    OH_WEAPON_233(81,43,74),
    TH_INT_WEAPON_233(162,386,147),
    OH_INT_WEAPON_233(81,252,74),
    OFF_INT_WEAPON_233(81,133,74),

    ;
    ItemStatTemplate(int stamina, int mainStat, int secondaryStat){
        this.stamina = stamina;
        this.mainStat = mainStat;
        this.secondaryStat = secondaryStat;
    }

    private int stamina;
    private int mainStat;
    private int secondaryStat;

    public static ItemStatTemplate getTemplateForPieceAndIlvl(ItemSlots slot, int ilvl){
        boolean high = slot == ItemSlots.CHEST
                || slot == ItemSlots.HEAD
                || slot == ItemSlots.LEGS
                || slot == ItemSlots.TH_WEAPON
                || slot == ItemSlots.RANGED;

        boolean medium = slot == ItemSlots.WAIST
                || slot == ItemSlots.SHOULDERS
                || slot == ItemSlots.FEET
                || slot == ItemSlots.HANDS;

        boolean low = slot == ItemSlots.BACK
                || slot == ItemSlots.WRISTS;

        boolean amulet = slot == ItemSlots.NECK
                || slot == ItemSlots.FINGER;

        boolean thIntWeapon = slot == ItemSlots.TH_INT_WEAPON;
        boolean offHandInt = slot == ItemSlots.OFF_HAND_ITEM;
        boolean trinket = slot == ItemSlots.TRINKET;
        boolean ohWeapon = slot == ItemSlots.OH_WEAPON;
        boolean ohIntWeapon = slot == ItemSlots.OH_INT_WEAPON
                || slot == ItemSlots.RANGED_INT;

        if(high){
            if(ilvl == 220)
                return HIGH_220;
            else if(ilvl == 226)
                return HIGH_226;
            else if(ilvl == 233)
                return HIGH_233;
        }
        else if(medium){
            if(ilvl == 220)
                return MEDIUM_220;
            else if(ilvl == 226)
                return MEDIUM_226;
            else if(ilvl == 233)
                return MEDIUM_233;
        }
        else if(low){
            if(ilvl == 220)
                return LOW_220;
            else if(ilvl == 226)
                return LOW_226;
            else if(ilvl == 233)
                return LOW_233;
        }
        else if(amulet){
            if(ilvl == 220)
                return AMULET_220;
            else if(ilvl == 226)
                return AMULET_226;
            else if(ilvl == 233)
                return AMULET_233;
        }
        else if(trinket){
            if(ilvl == 220)
                return TRINKET_220;
            else if(ilvl == 226)
                return TRINKET_226;
            else if(ilvl == 233)
                return TRINKET_233;
        }
        else if(ohWeapon){
            if(ilvl == 220)
                return OH_WEAPON_220;
            else if(ilvl == 226)
                return OH_WEAPON_226;
            else if(ilvl == 233)
                return OH_WEAPON_233;
        }
        else if(ohIntWeapon){
            if(ilvl == 220)
                return OH_INT_WEAPON_220;
            else if(ilvl == 226)
                return OH_INT_WEAPON_226;
            else if(ilvl == 233)
                return OH_INT_WEAPON_233;
        }
        else if(thIntWeapon){
            if(ilvl == 220)
                return TH_INT_WEAPON_220;
            else if(ilvl == 226)
                return TH_INT_WEAPON_226;
            else if(ilvl == 233)
                return TH_INT_WEAPON_233;
        }
        else if(offHandInt){
            if(ilvl == 220)
                return OFF_INT_WEAPON_220;
            else if(ilvl == 226)
                return OFF_INT_WEAPON_226;
            else if(ilvl == 233)
                return OFF_INT_WEAPON_233;
        }
        return UNDEFINED;
    }

}
