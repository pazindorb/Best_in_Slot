package pl.bloniarz.bis.model.dto.response.character;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterResponse {
    private long id;
    private String name;
    private String characterClass;
    private int numberOfSets;
}
