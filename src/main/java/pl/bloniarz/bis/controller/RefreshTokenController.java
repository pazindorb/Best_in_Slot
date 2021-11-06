package pl.bloniarz.bis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.bloniarz.bis.service.IRefreshTokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/refresh")
public class RefreshTokenController {

    private final IRefreshTokenService refreshTokenService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void refreshToken(HttpServletRequest request, HttpServletResponse response){
        refreshTokenService.refresh(request, response);
    }


}
