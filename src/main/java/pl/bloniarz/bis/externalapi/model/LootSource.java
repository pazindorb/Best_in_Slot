package pl.bloniarz.bis.externalapi.model;

import lombok.Getter;

@Getter
public enum LootSource {
    SL_DOS("De Other Side", "220:195;13309:1;0:0", 158, 158, "Shadowlands"),
    SL_HOA("Halls of Atonement", "220:195;12831:1;0:0", 158, 158, "Shadowlands"),
    SL_MIST("Mists of Trina Scithe", "220:195;13334:1;0:0", 158, 158, "Shadowlands"),
    SL_PF("Plaguefall", "220:195;13228:1;0:0", 158, 158, "Shadowlands"),
    SL_SD("Sanguine Depths", "220:195;12842:1;0:0", 158, 158, "Shadowlands"),
    SL_SOA("Spires of Ascension", "220:195;12837:1;0:0", 158, 158, "Shadowlands"),
    SL_NW("The Necrotic Wake", "220:195;12916:1;0:0", 158, 158, "Shadowlands"),
    SL_TOP("Theater of Pain", "220:195;12841:1;0:0", 158, 158, "Shadowlands"),
    SL_R_CN("Castle Nathria", "212:195;13224:1;0:0", 200, 207, "Shadowlands"),
    SL_PVP("PvP", "129:195;0:1;168011:0", 200, 200, "Shadowlands");

    LootSource(String sourceName, String searchRequirements, int minIlvl, int maxIlvl, String expansion) {
        this.sourceName = sourceName;
        this.searchRequirements = searchRequirements;
        this.minIlvl = minIlvl;
        this.maxIlvl = maxIlvl;
        this.expansion = expansion;
    }

    private final String sourceName;
    private final String searchRequirements;
    private final String expansion;
    private final int minIlvl;
    private final int maxIlvl;


}

