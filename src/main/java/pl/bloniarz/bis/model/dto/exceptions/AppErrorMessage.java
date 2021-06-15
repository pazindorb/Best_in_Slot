package pl.bloniarz.bis.model.dto.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AppErrorMessage {

    USER_ALREADY_IN_DATABASE("User with this email or login already exists.", 409),
    VALIDATION_FAILED("Validation restrictions failed",400),


    ;
    private final String message;
    private final int status;

}
