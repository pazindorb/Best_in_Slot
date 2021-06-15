package pl.bloniarz.bis.externalapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WowheadItemResponseStat {

    @JsonProperty("sta")
    private int stamina;

    @JsonProperty("int")
    private int intelligence;

    @JsonProperty("str")
    private int strength;

    @JsonProperty("agi")
    private int agility;

    private double secondary;

    @JsonProperty("mastrtng")
    private double mastery;

    @JsonProperty("critstrkrtng")
    private double criticalStrike;

    @JsonProperty("hastertng")
    private double haste;

    @JsonProperty("versatility")
    private double versatility;


    public void prepareProcentSecondaryStats(){
        secondary += mastery + criticalStrike + haste + versatility;

        if(!(mastery == 0.0))
            mastery = mastery/secondary;

        if(!(criticalStrike == 0.0))
            criticalStrike = criticalStrike/secondary;

        if(!(haste == 0.0))
            haste = haste/secondary;

        if(!(versatility == 0.0))
            versatility = versatility/secondary;
    }

}
