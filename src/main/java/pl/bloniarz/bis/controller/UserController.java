package pl.bloniarz.bis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.bloniarz.bis.model.dto.request.user.UserLoginRequest;
import pl.bloniarz.bis.model.dto.request.user.UserRegisterRequest;
import pl.bloniarz.bis.model.dto.response.LoginSuccessfullyResponse;
import pl.bloniarz.bis.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void registerUser(@Valid @RequestBody UserRegisterRequest userRegistrationRequest){
        userService.registerUser(userRegistrationRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginSuccessfullyResponse> loginUser(@RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response){
        Cookie cookie = new Cookie("authorization", userService.loginUser(userLoginRequest));
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity
                .ok()
                .body(new LoginSuccessfullyResponse(userLoginRequest.getLogin()));
    }

}
