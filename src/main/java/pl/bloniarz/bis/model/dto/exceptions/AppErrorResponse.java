package pl.bloniarz.bis.model.dto.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class AppErrorResponse {

    private final String message;
    private final String errorTime;

    public AppErrorResponse(String message){
        this.message = message;
        this.errorTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

}
