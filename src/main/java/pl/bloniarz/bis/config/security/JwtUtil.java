package pl.bloniarz.bis.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pl.bloniarz.bis.model.dao.user.UserAuthorityEntity;
import pl.bloniarz.bis.model.dao.user.UserEntity;
import pl.bloniarz.bis.model.dto.exceptions.AppErrorMessage;
import pl.bloniarz.bis.model.dto.exceptions.AppException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final String secret;
    private final long accessTokenExpireTime;
    private final long refreshTokenExpireTime;
    private final String issuer;
    private final Algorithm algorithm;

    public JwtUtil(){
        this.secret = "gpoauv23124favngo!3213";
        this.accessTokenExpireTime = 1000 * 60;
        this.refreshTokenExpireTime = 1000 * 60 * 24 * 7;
        this.issuer = "best_in_slot";
        this.algorithm = Algorithm.HMAC256(secret.getBytes());
    }

    public String createAccessToken(UserDetails appUser){
        return JWT.create()
                .withSubject(appUser.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpireTime))
                .withIssuer(issuer)
                .withClaim("roles", appUser.getAuthorities().stream()
                        .map(GrantedAuthority::toString)
                        .collect(Collectors.toList()))
                .sign(algorithm);
    }

    public String createRefreshToken(UserDetails appUser) {
        return JWT.create()
                .withSubject(appUser.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpireTime))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public DecodedJWT verifyTokenFromCookie(String cookieName, Cookie[] cookies) {
        Cookie cookie = extractCookieFromArray(cookieName, cookies);

        String token = cookie.getValue();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT;
        try{
            decodedJWT = verifier.verify(token);
        } catch (Exception exception){
            throw new AppException(AppErrorMessage.VERIFICATION_FAILED, exception.getMessage());
        }
        return decodedJWT;
    }

    public Date getExpirationDateFromHeaderToken(DecodedJWT token){
        return token.getExpiresAt();
    }

    public Cookie createAccessTokenCookie(UserDetails appUser) {
        String accessToken = createAccessToken(appUser);
        return createTokenCookie("access_token", accessToken);
    }

    public Cookie createRefreshTokenCookie(UserDetails appUser) {
        String refreshToken = createRefreshToken(appUser);
        return createTokenCookie("refresh_token", refreshToken);
    }

    public void removeAccessTokenCookie(HttpServletResponse response){
        removeCookie(response, "access_token");
    }

    public void removeRefreshTokenCookie( HttpServletResponse response){
        removeCookie(response, "refresh_token");
    }

    private void removeCookie(HttpServletResponse response, String cookieName){
        Cookie cookie = new Cookie(cookieName, "remove");
        cookie.setMaxAge(0);
        cookie.setPath("/api/");
        response.addCookie(cookie);
    }

    private Cookie createTokenCookie(String name, String token){
        Cookie cookie = new Cookie(name, token);
        cookie.setPath("/api/");
        //cookie.setSecure(true);
        cookie.setHttpOnly(true);
        return cookie;
    }

    private Cookie extractCookieFromArray(String cookieName, Cookie[] cookies){
        if(cookies == null)
            throw new AppException(AppErrorMessage.UNAUTHORIZED);

        return Arrays.stream(cookies)
                .filter(c -> cookieName.equals(c.getName()))
                .findAny()
                .orElseThrow(() -> new AppException(AppErrorMessage.UNAUTHORIZED));
    }
}
