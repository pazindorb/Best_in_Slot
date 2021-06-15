package pl.bloniarz.bis.model.dao.item.enums;

//Subclass
public enum ItemTypes {
    UNDEFINED(""),

    CLOAK("Cloaks"),
    CLOTH("Cloth Armor"),
    LEATHER("Leather Armor"),
    MAIL("Mail Armor"),
    PLATE("Plate Armor"),

    RING("Rings"),
    NECK("Amulets"),
    TRINKET("Trinkets"),

    OFF_HAND("Off-hand Frills"),
    SHIELD("Shields"),

    WARGLAVE("Warglaives"),
    DAGGER("Daggers"),
    FIST_WEAPON("Fist Weapons"),
    OH_SWORD("One-Handed Swords"),
    OH_MACE("One-Handed Maces"),
    OH_AXE("One-Handed Axes"),

    TH_SWORD("Two-Handed Swords"),
    TH_MACE("Two-Handed Maces"),
    TH_AXE("Two-Handed Axes"),
    POLEARM("Polearms"),
    STAFF("Staves"),

    BOW("Bows"),
    CROSSBOW("Crossbows"),
    GUN("Guns"),
    WAND( "Wands"),

    ;

    ItemTypes(String name){
        this.name = name;
    }

    final String name;

    public static ItemTypes fromName(String name){
        ItemTypes[] allValues = ItemTypes.values();

        for (ItemTypes allValue : allValues) {
            if (allValue.name.equals(name))
                return allValue;
        }
        return UNDEFINED;
    }





}
