package pl.bloniarz.bis.model.dto.response.character;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.bloniarz.bis.model.dto.response.character.CharacterResponse;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersCharactersResponse {
    private List<CharacterResponse> characters;
}
