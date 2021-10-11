package pl.bloniarz.bis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.bloniarz.bis.model.dto.exceptions.AppErrorMessage;
import pl.bloniarz.bis.model.dto.exceptions.AppException;
import pl.bloniarz.bis.model.dto.request.user.UserLoginRequest;
import pl.bloniarz.bis.model.dto.request.user.UserRegisterRequest;
import pl.bloniarz.bis.model.dto.response.SimpleMessageResponse;
import pl.bloniarz.bis.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public long register(@Valid @RequestBody UserRegisterRequest userRegistrationRequest){
        return userService.register(userRegistrationRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public void login(@RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response){
        userService.login(userLoginRequest, response);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/logout")
    public void logout(HttpServletResponse response){
        userService.logout(response);
    }

}
