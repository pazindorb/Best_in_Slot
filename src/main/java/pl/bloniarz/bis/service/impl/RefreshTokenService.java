package pl.bloniarz.bis.service.impl;


import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.bloniarz.bis.config.security.DetailsService;
import pl.bloniarz.bis.config.security.JwtUtil;
import pl.bloniarz.bis.service.IRefreshTokenService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements IRefreshTokenService {

    private final JwtUtil jwtUtil;
    private final DetailsService detailsService;

    @Override
    public void refresh(HttpServletRequest request, HttpServletResponse response) {
        DecodedJWT refreshToken = jwtUtil.verifyTokenFromCookie("refresh_token", request.getCookies());

        String username = refreshToken.getSubject();
        UserDetails user = detailsService.loadUserByUsername(username);
        Cookie accessCookie = jwtUtil.createAccessTokenCookie(user);

        response.addCookie(accessCookie);
    }
}
