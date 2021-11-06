package pl.bloniarz.bis.model.dto.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AppErrorMessage {

    USER_ALREADY_IN_DATABASE("User with this email or login already exists.", 409),
    EMAIL_USED("This email is already used.", 409),
    LOGIN_USED("This login is already used.", 409),
    VALIDATION_FAILED("Validation restrictions failed.",400),
    VERIFICATION_FAILED("You need to login again, verification failed. Specific information: %s.", 400),
    LOGIN_FAILED("Login failed, wrong username or password.", 404),
    LOGOUT_FAILED("Unexpected logout error, sorry.", 500),
    USER_NOT_FOUND("%s not found.", 404),
    USER_NOT_FOUND_ID("User not found.", 404),
    UNAUTHORIZED("You need to login first.",401),
    NOT_OWNER("You are not rightful owner of this.", 400),
    CHARACTER_NOT_FOUND("You dont have character named: %s.", 404),
    CHARACTER_NOT_FOUND_ID("Character with that id do not exists", 404),
    CHARACTER_ALREADY_EXISTS("Character with this name already exists.", 409),
    WRONG_PASSWORD("Your password is not correct", 403),
    SET_NOT_FOUND("Character have no such set.", 404),
    SLOT_NOT_FOUND("Slot not found.", 404),
    ITEM_NOT_FOUND("Item not found.", 404),

    ;
    private final String message;
    private final int status;

}
