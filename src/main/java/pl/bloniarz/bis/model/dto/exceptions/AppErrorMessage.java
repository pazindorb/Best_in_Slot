package pl.bloniarz.bis.model.dto.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AppErrorMessage {

    USER_ALREADY_IN_DATABASE("User with this email or login already exists.", 409),
    VALIDATION_FAILED("Validation restrictions failed",400),
    VERIFICATION_FAILED("You need to login again, verification failed. Specific information: %s", 400),
    LOGIN_FAILED("Login failed, wrong username or password", 400),
    LOGOUT_FAILED("Unexpectet logout errror, sorry", 500),

    ;
    private final String message;
    private final int status;

}
