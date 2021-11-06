package pl.bloniarz.bis.model.dto.request.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {


    private String email;

    private String login;
    private String oldPassword;

    private String newPassword;
}
