package pl.bloniarz.bis.model.dao.character;

import lombok.Getter;

@Getter
public enum CharacterClasses {
    WARRIOR,
    HUNTER,
    MAGE,
    ROGUE,
    PRIEST,
    WARLOCK,
    PALADIN,
    DRUID,
    SHAMAN,
    MONK,
    DEMON_HUNTER,
    DEATH_KNIGHT;

    private String name;
}
