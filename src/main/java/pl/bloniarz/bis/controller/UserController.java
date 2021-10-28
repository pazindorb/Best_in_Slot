package pl.bloniarz.bis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.bloniarz.bis.model.dto.request.user.UserLoginRequest;
import pl.bloniarz.bis.model.dto.request.user.UserPasswordCheck;
import pl.bloniarz.bis.model.dto.request.user.UserRegisterRequest;
import pl.bloniarz.bis.model.dto.request.user.UserUpdateRequest;
import pl.bloniarz.bis.model.dto.response.user.UserResponse;
import pl.bloniarz.bis.service.IUserService;
import pl.bloniarz.bis.service.impl.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final IUserService userService;


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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserResponse> getUsers(@RequestParam int page, @RequestParam(required = false) String sortBy, @RequestParam(required = false) boolean desc){
        return userService.getUsers(page, sortBy, desc);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable long id){
        return userService.getUser(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteUserAdmin(@PathVariable long id, Principal principal, @RequestBody UserPasswordCheck password){
        userService.deleteUserAdmin(id, principal.getName(), password);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping()
    public void deleteUser(Principal principal, @RequestBody UserPasswordCheck password){
        userService.deleteUserAdmin(principal.getName(), password);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping
    public void updateUser(Principal principal, @RequestBody UserUpdateRequest userUpdateRequest){
        userService.updateUser(principal.getName(), userUpdateRequest);
    }

}
