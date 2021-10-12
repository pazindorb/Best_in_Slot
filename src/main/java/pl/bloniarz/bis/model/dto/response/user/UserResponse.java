package pl.bloniarz.bis.model.dto.response.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private long id;
    private String login;
    private int numberOfCharacters;
    private List<String> authorities;

}
