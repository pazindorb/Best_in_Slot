package pl.bloniarz.bis.model.dto.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AppErrorMessage {

    USER_ALREADY_IN_DATABASE("User with this email or login already exists.", 409),
    VALIDATION_FAILED("Validation restrictions failed",400),
    VERIFICATION_FAILED("You need to login again, verification failed. Specific information: %s", 400),
    LOGIN_FAILED("Login failed, wrong username or password", 404),
    LOGOUT_FAILED("Unexpectet logout errror, sorry", 500),
    USER_NOT_FOUND("%s not found in database", 404),
    COOKIE_NOT_FOUND("Authorization cookie not found",404),
    NOT_OWNER("You are not rightful owner of this", 400),
    CHARACTER_NOT_FOUND("You dont have character named: %s", 404),

    ;
    private final String message;
    private final int status;

}
