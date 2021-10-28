package pl.bloniarz.bis.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import pl.bloniarz.bis.config.security.filter.FilterExceptionHandler;
import pl.bloniarz.bis.config.security.filter.JwtFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpMethod.GET;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService detailsService;
    private final JwtFilter jwtFilter;
    private final FilterExceptionHandler filterExceptionHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(detailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(POST,
                        "/api/users",
                        "/api/users/login",
                        "/api/users/logout",
                        "/api/refresh"
                ).permitAll()

                .antMatchers(GET,
                        "/api/characters/{id}",
                        "/api/{character}/{id}",
                        "/api/items",
                        "/api/users",
                        "/api/users/{id}",
                        "/api/characters/character/{id}"
                ).permitAll()


                .antMatchers(PATCH,
                        "/api/items/**"
                ).hasAuthority("ROLE_ADMIN")
                .antMatchers(POST,
                        "/api/items/**"
                ).hasAuthority("ROLE_ADMIN")
                .antMatchers(DELETE,
                        "/api/users/{id}"
                ).hasAuthority("ROLE_ADMIN")

                .anyRequest().authenticated()
        ;

        http
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .exceptionHandling()
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))

                .and()
                .addFilterBefore(filterExceptionHandler, LogoutFilter.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
