package pl.bloniarz.bis.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.bloniarz.bis.model.dto.exceptions.AppErrorMessage;
import pl.bloniarz.bis.model.dto.exceptions.AppErrorResponse;
import pl.bloniarz.bis.model.dto.exceptions.AppException;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler({AppException.class})
    public ResponseEntity<AppErrorResponse> handleAppException(AppException appException){
        log.error("[{}] Message: {}", appException.getClass().getSimpleName(), appException.getMessage());
        return ResponseEntity
                .status(appException.getResponseStatus())
                .body(new AppErrorResponse(appException.getMessage()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<AppErrorResponse> handleValidationException(MethodArgumentNotValidException exception){
        log.error("[{}] Message: {}", exception.getClass().getSimpleName(), exception.getMessage());
        return ResponseEntity
                .status(AppErrorMessage.VALIDATION_FAILED.getStatus())
                .body(new AppErrorResponse(AppErrorMessage.VALIDATION_FAILED.getMessage()));
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<AppErrorResponse> handleBadCredentialsException(BadCredentialsException exception){
        log.error("[{}] Message: {}", exception.getClass().getSimpleName(), exception.getMessage());
        return ResponseEntity
                .status(AppErrorMessage.VALIDATION_FAILED.getStatus())
                .body(new AppErrorResponse(AppErrorMessage.VALIDATION_FAILED.getMessage()));
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<AppErrorResponse> handleUnsupportedException(RuntimeException runtimeException){
        log.error("[{}] Message: {}", runtimeException.getClass().getSimpleName(), runtimeException.getMessage());
        return ResponseEntity
                .status(500)
                .body(new AppErrorResponse("An unsupported exception occurred, we are sorry."));
    }

}
