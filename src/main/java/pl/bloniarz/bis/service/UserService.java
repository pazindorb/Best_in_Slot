package pl.bloniarz.bis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.bloniarz.bis.config.security.jwt.JwtUtil;
import pl.bloniarz.bis.model.dao.user.UserAuthorityEntity;
import pl.bloniarz.bis.model.dao.user.UserEntity;
import pl.bloniarz.bis.model.dto.exceptions.AppErrorMessage;
import pl.bloniarz.bis.model.dto.exceptions.AppException;
import pl.bloniarz.bis.model.dto.request.user.UserLoginRequest;
import pl.bloniarz.bis.model.dto.request.user.UserRegisterRequest;
import pl.bloniarz.bis.repository.AuthorityRepository;
import pl.bloniarz.bis.repository.UserRepository;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService detailsService;
    private final JwtUtil jwtUtil;

    public void register(UserRegisterRequest userRegisterRequest){
        List<UserAuthorityEntity> authority = Collections.singletonList(authorityRepository.findByAuthority("ROLE_USER"));

        if(userRepository.findByLogin(userRegisterRequest.getLogin()).isPresent() ||
                userRepository.findByEmail(userRegisterRequest.getEmail()).isPresent())
            throw new AppException(AppErrorMessage.USER_ALREADY_IN_DATABASE);

        userRepository.save(UserEntity.builder()
                .authorities(authority)
                .login(userRegisterRequest.getLogin())
                .password(passwordEncoder.encode(userRegisterRequest.getPassword()))
                .email(userRegisterRequest.getEmail())
                .charactersList(new ArrayList<>())
                .build());
    }


    public Cookie login(UserLoginRequest userLoginRequest) {
        try{
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            userLoginRequest.getLogin(),
                            userLoginRequest.getPassword()
                    ));
        } catch(Exception e) {
            throw new AppException(AppErrorMessage.LOGIN_FAILED);
        }
        UserDetails user = detailsService.loadUserByUsername(userLoginRequest.getLogin());
        Cookie cookie = new Cookie("authorization", jwtUtil.generateToken(user) );
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setPath("/");
        return cookie;
    }

    public Cookie logout(Cookie cookie){
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }
}
