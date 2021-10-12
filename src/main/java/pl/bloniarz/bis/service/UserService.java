package pl.bloniarz.bis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bloniarz.bis.config.security.JwtUtil;
import pl.bloniarz.bis.model.dao.user.UserAuthorityEntity;
import pl.bloniarz.bis.model.dao.user.UserEntity;
import pl.bloniarz.bis.model.dto.exceptions.AppErrorMessage;
import pl.bloniarz.bis.model.dto.exceptions.AppException;
import pl.bloniarz.bis.model.dto.request.user.UserLoginRequest;
import pl.bloniarz.bis.model.dto.request.user.UserPasswordCheck;
import pl.bloniarz.bis.model.dto.request.user.UserRegisterRequest;
import pl.bloniarz.bis.model.dto.request.user.UserUpdateRequest;
import pl.bloniarz.bis.model.dto.response.user.UserResponse;
import pl.bloniarz.bis.repository.AuthorityRepository;
import pl.bloniarz.bis.repository.CharacterRepository;
import pl.bloniarz.bis.repository.UserRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.xml.validation.Validator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CharacterRepository characterRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService detailsService;
    private final JwtUtil jwtUtil;

    public long register(UserRegisterRequest userRegisterRequest){
        List<UserAuthorityEntity> authority = Collections.singletonList(authorityRepository.findByAuthority("ROLE_USER"));

        if(userRepository.findByLogin(userRegisterRequest.getLogin()).isPresent() ||
                userRepository.findByEmail(userRegisterRequest.getEmail()).isPresent())
            throw new AppException(AppErrorMessage.USER_ALREADY_IN_DATABASE);

        return userRepository.save(UserEntity.builder()
                .authorities(authority)
                .login(userRegisterRequest.getLogin())
                .password(passwordEncoder.encode(userRegisterRequest.getPassword()))
                .email(userRegisterRequest.getEmail())
                .charactersList(new ArrayList<>())
                .active(true)
                .build()).getId();
    }


    public void login(UserLoginRequest userLoginRequest, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userLoginRequest.getLogin(), userLoginRequest.getPassword());

        if(!authenticationManager.authenticate(authenticationToken).isAuthenticated())
            throw new AppException(AppErrorMessage.LOGIN_FAILED);

        UserDetails user = detailsService.loadUserByUsername(userLoginRequest.getLogin());

        Cookie accessCookie = jwtUtil.createAccessTokenCookie(user);
        Cookie refreshCookie = jwtUtil.createRefreshTokenCookie(user);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }

    public void logout(HttpServletResponse response){
        jwtUtil.removeAccessTokenCookie(response);
        jwtUtil.removeRefreshTokenCookie(response);
    }

    public List<UserResponse> getUsers(int pageNumber, String sortBy, boolean descending) {
        int pageSize = 10;
        Pageable page;

        if(sortBy == null)
            page = PageRequest.of(pageNumber, pageSize);
        else if(!descending)
            page = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        else
            page = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());

        List<UserEntity> entityList = userRepository.findByActiveIsTrue(page);

        return entityList.stream()
                .map(this::parseUserEntityToUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUser(long id) {
        UserEntity userEntity = userRepository.findByIdAndActiveIsTrue(id)
                .orElseThrow(() -> new AppException(AppErrorMessage.USER_NOT_FOUND_ID));

        return parseUserEntityToUserResponse(userEntity);
    }

    @Transactional
    public void deleteUserAdmin(long id, String adminName, UserPasswordCheck password) {
        UserEntity admin = userRepository.findByLoginAndActiveIsTrue(adminName)
                .orElseThrow(() -> new AppException(AppErrorMessage.USER_NOT_FOUND, "Admin account"));

        if(!verifyPassword(password.getPassword(), admin.getPassword()))
            throw new AppException(AppErrorMessage.WRONG_PASSWORD);

        UserEntity userEntity = userRepository.findByIdAndActiveIsTrue(id)
                .orElseThrow(() -> new AppException(AppErrorMessage.USER_NOT_FOUND_ID));

        userEntity.delete();
        userRepository.save(userEntity);
    }

    @Transactional
    public void deleteUserAdmin(String username, UserPasswordCheck password) {
        UserEntity userEntity = userRepository.findByLoginAndActiveIsTrue(username)
                .orElseThrow(() -> new AppException(AppErrorMessage.USER_NOT_FOUND_ID));

        if(!verifyPassword(password.getPassword(), userEntity.getPassword()))
            throw new AppException(AppErrorMessage.WRONG_PASSWORD);

        userEntity.delete();
        userRepository.save(userEntity);
    }

    public void updateUser(String login, UserUpdateRequest userUpdateRequest) {
        UserEntity userEntity = userRepository.findByLoginAndActiveIsTrue(login)
                .orElseThrow(() ->  new AppException(AppErrorMessage.USER_NOT_FOUND_ID));

        if(!verifyPassword(userUpdateRequest.getOldPassword(), userEntity.getPassword()))
            throw new AppException(AppErrorMessage.WRONG_PASSWORD);

        if(!(userUpdateRequest.getNewPassword() == null || userUpdateRequest.getNewPassword().equals("")))
        {
            Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–{}:;',?/*~$^+=<>]).{8,20}$");
            if(!passwordPattern.matcher(userUpdateRequest.getNewPassword()).matches())
                throw new AppException(AppErrorMessage.VALIDATION_FAILED);
            userEntity.setPassword(passwordEncoder.encode(userUpdateRequest.getNewPassword()));
        }

        if(!(userUpdateRequest.getEmail() == null || userUpdateRequest.getEmail().equals("")))
        {
            Pattern emailPattern = Pattern.compile("^[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
            if(!emailPattern.matcher(userUpdateRequest.getEmail()).matches())
                throw new AppException(AppErrorMessage.VALIDATION_FAILED);

            if(userRepository.findByEmailAndActiveIsTrue(userUpdateRequest.getEmail()).isPresent())
                throw new AppException(AppErrorMessage.EMAIL_USED);

            userEntity.setEmail(userUpdateRequest.getEmail());
        }

        if(!(userUpdateRequest.getLogin() == null || userUpdateRequest.getLogin().equals("")))
        {
            if(userRepository.findByLoginAndActiveIsTrue(userUpdateRequest.getLogin()).isPresent())
                throw new AppException(AppErrorMessage.LOGIN_USED);
            userEntity.setLogin(userUpdateRequest.getLogin());
        }

        userRepository.save(userEntity);
    }

    private UserResponse parseUserEntityToUserResponse(UserEntity entity){
        return UserResponse.builder()
                .id(entity.getId())
                .login(entity.getLogin())
                .authorities(entity.getAuthorities().stream()
                        .map(UserAuthorityEntity::getAuthority)
                        .collect(Collectors.toList()))
                .numberOfCharacters(characterRepository.getCountOfCharactersForUserId(entity.getId()))
                .build();
    }

    private boolean verifyPassword(String password, String encodedPassword){
        return passwordEncoder.matches(password, encodedPassword);
    }


}
