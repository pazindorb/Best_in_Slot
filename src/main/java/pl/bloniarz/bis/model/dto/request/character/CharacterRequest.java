package pl.bloniarz.bis.model.dto.request.character;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterRequest {
    private String name;
    private String characterClass;
    private int numberOfSets;
}
