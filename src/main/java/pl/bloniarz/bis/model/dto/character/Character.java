package pl.bloniarz.bis.model.dto.character;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Character {
    private String name;
    private String characterClass;
    private int numberOfSets;
}
