package pl.bloniarz.bis.externalapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LootSource {

    private String sourceName;
    private String searchRequirements;
    private String expansion;
    private int minIlvl;
    private int maxIlvl;

}
