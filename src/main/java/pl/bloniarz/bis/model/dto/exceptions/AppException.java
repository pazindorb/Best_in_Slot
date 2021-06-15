package pl.bloniarz.bis.model.dto.exceptions;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException{

    private int responseStatus;

    public AppException(AppErrorMessage appErrorMessage, String... params){
        super(String.format(appErrorMessage.getMessage(),params));
        this.responseStatus = appErrorMessage.getStatus();
    }

}
