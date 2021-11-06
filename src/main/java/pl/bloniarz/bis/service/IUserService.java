package pl.bloniarz.bis.service;

import org.springframework.transaction.annotation.Transactional;
import pl.bloniarz.bis.model.dto.request.user.UserLoginRequest;
import pl.bloniarz.bis.model.dto.request.user.UserPasswordCheck;
import pl.bloniarz.bis.model.dto.request.user.UserRegisterRequest;
import pl.bloniarz.bis.model.dto.request.user.UserUpdateRequest;
import pl.bloniarz.bis.model.dto.response.user.UserResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IUserService {
    long register(UserRegisterRequest userRegisterRequest);

    void login(UserLoginRequest userLoginRequest, HttpServletResponse response);

    void logout(HttpServletResponse response);

    List<UserResponse> getUsers(int pageNumber, String sortBy, boolean descending);

    UserResponse getUser(long id);

    void deleteUserAdmin(long id, String adminName, UserPasswordCheck password);

    void deleteUserAdmin(String username, UserPasswordCheck password);

    void updateUser(String login, UserUpdateRequest userUpdateRequest);
}
