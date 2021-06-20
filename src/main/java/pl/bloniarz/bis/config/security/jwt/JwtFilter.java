package pl.bloniarz.bis.config.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.bloniarz.bis.model.dto.exceptions.AppErrorMessage;
import pl.bloniarz.bis.model.dto.exceptions.AppException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final UserDetailsService detailsService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = jwtUtil.extractTokenFromCookies(request.getCookies());
        String username = jwtUtil.extractUserName(token);

        if (username == null)
            throw new AppException(AppErrorMessage.VERIFICATION_FAILED, "Could not extract username from token.");
        if(SecurityContextHolder.getContext().getAuthentication() != null)
            throw new AppException(AppErrorMessage.VERIFICATION_FAILED, "SecurityContextHolder.getContext().getAuthentication() != null.");
        UserDetails userDetails = detailsService.loadUserByUsername(username);

        if (!jwtUtil.validateToken(token, userDetails))
            throw new AppException(AppErrorMessage.VERIFICATION_FAILED, "Token cannot be validated.");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {
        String path = request.getRequestURI();
        Boolean notFilteredPaths = "/api/users".equals(path)
                || "/api/users/login".equals(path)
                ;

        return notFilteredPaths;
    }
}