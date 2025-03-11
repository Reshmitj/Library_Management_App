package edu.saumc.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import edu.saumc.library.repository.AdminRepository;
import edu.saumc.library.repository.UserRepository;
import edu.saumc.library.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Configuration
public class SecurityConfig {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public SecurityConfig(AuthService authService, UserRepository userRepository, AdminRepository adminRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/register", "/user/register", "/login", "/css/**", "/h2-console/**").permitAll()
                .requestMatchers("/admin/**").authenticated()
                .anyRequest().authenticated())
            .formLogin(login -> login
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(customSuccessHandler())
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll())
            .authenticationProvider(customAuthenticationProvider())
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            try {
                String email = authentication.getName();
                if (adminRepository.findByEmail(email).isPresent()) {
                    response.sendRedirect("/admin/dashboard");
                } else {
                    response.sendRedirect("/home");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    @Bean
    public AuthenticationProvider customAuthenticationProvider() {
        return new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String email = authentication.getName();
                String password = authentication.getCredentials().toString();

                if (authService.authenticateAdmin(email, password)) {
                    return new UsernamePasswordAuthenticationToken(email, password, 
                            Collections.singletonList((GrantedAuthority) () -> "ROLE_ADMIN"));
                }

                if (authService.authenticateUser(email, password)) {
                    return new UsernamePasswordAuthenticationToken(email, password, 
                            Collections.singletonList((GrantedAuthority) () -> "ROLE_USER"));
                }

                return null;
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return authentication.equals(UsernamePasswordAuthenticationToken.class);
            }
        };
    }
}
