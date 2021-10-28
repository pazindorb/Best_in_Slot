package pl.bloniarz.bis.config.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.bloniarz.bis.config.security.JwtUtil;
import pl.bloniarz.bis.model.dto.exceptions.AppErrorMessage;
import pl.bloniarz.bis.model.dto.exceptions.AppException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Configuration
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        DecodedJWT accessToken = jwtUtil.verifyTokenFromCookie("access_token", request.getCookies());

        String username = accessToken.getSubject();
        String[] roles = accessToken.getClaim("roles").asArray(String.class);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Arrays.stream(roles)
                .forEach(role ->
                        authorities.add(new SimpleGrantedAuthority(role))
                );

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        Pattern getCharacter = Pattern.compile("/api/characters/.+", Pattern.DOTALL);
        Pattern getItemSet = Pattern.compile("/api/.+/.+", Pattern.DOTALL);

        boolean notFilteredPaths =
                        ("/api/users".equals(path) && "POST".equals(method)) ||
                        ("/api/users/login".equals(path) && "POST".equals(method)) ||
                        ("/api/users/logout".equals(path) && "POST".equals(method)) ||
                        ("/api/refresh".equals(path) && "POST".equals(method)) ||
                        ("/api/items".equals(path) && "GET".equals(method)) ||
                        ("/api/users".equals(path) && "GET".equals(method)) ||
                        (getCharacter.matcher(path).matches() && "GET".equals(method)) ||
                        (getItemSet.matcher(path).matches() && "GET".equals(method))
                ;

        return notFilteredPaths;
    }
}