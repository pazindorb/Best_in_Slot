package pl.bloniarz.bis.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.bloniarz.bis.model.dto.exceptions.AppErrorMessage;
import pl.bloniarz.bis.model.dto.exceptions.AppErrorResponse;
import pl.bloniarz.bis.model.dto.exceptions.AppException;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler({AppException.class})
    public ResponseEntity<AppErrorResponse> handleAppException(AppException appException){
        return ResponseEntity
                .status(appException.getResponseStatus())
                .body(new AppErrorResponse(appException.getMessage()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<AppErrorResponse> handleValidationException(){
        return ResponseEntity
                .status(AppErrorMessage.VALIDATION_FAILED.getStatus())
                .body(new AppErrorResponse(AppErrorMessage.VALIDATION_FAILED.getMessage()));
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<AppErrorResponse> handleUnsupportedException(){
        return ResponseEntity
                .status(500)
                .body(new AppErrorResponse("An unsupported exception occurred, we are sorry."));
    }

}
