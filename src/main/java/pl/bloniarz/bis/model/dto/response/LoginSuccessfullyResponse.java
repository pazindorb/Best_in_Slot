package pl.bloniarz.bis.model.dto.response;

import lombok.Getter;

@Getter
public class LoginSuccessfullyResponse {

    private final String message;


    public LoginSuccessfullyResponse(String username){
        message = "Login successful for: " + username;
    }

}
