package pl.bloniarz.bis.config.security.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.bloniarz.bis.model.dto.exceptions.AppErrorMessage;
import pl.bloniarz.bis.model.dto.exceptions.AppErrorResponse;
import pl.bloniarz.bis.model.dto.exceptions.AppException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@Slf4j
public class FilterExceptionHandler extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AppException appException) {
            log.error("[{}] Message: {}", appException.getClass().getSimpleName(), appException.getMessage());
            response.setStatus(appException.getResponseStatus());
            response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            response.getOutputStream().print(new ObjectMapper().writeValueAsString(new AppErrorResponse(appException.getMessage())));

        } catch (RuntimeException e) {
            log.error("[{}] Message: {}", e.getClass().getSimpleName(), e.getMessage());
            String message = String.format(AppErrorMessage.VERIFICATION_FAILED.getMessage(), e.getMessage());
            response.setStatus(500);
            response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            response.getOutputStream().print(new ObjectMapper().writeValueAsString(new AppErrorResponse(message)));
        }
    }
}
