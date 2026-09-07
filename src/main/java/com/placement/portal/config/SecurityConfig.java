package com.placement.portal.config;

import com.placement.portal.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/auth/**", "/error").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/student/**").hasRole("STUDENT")
                        .requestMatchers("/company/**").hasRole("COMPANY")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((req, res, auth) -> {
                            String role = auth.getAuthorities().iterator().next().getAuthority();
                            if (role.equals("ROLE_ADMIN"))        res.sendRedirect("/admin/dashboard");
                            else if (role.equals("ROLE_STUDENT")) res.sendRedirect("/student/dashboard");
                            else if (role.equals("ROLE_COMPANY")) res.sendRedirect("/company/dashboard");
                            else res.sendRedirect("/");
                        })
                        .failureUrl("/auth/login?error=true")
                        .permitAll()
                )
                // Keeps the user logged in across browser/tab restarts using a persistent
                // "remember-me" cookie, instead of relying only on the in-memory session
                // cookie that disappears the moment the browser fully closes.
                .rememberMe(remember -> remember
                        .key("placehub-remember-me-key")
                        .userDetailsService(userDetailsService)
                        .tokenValiditySeconds(14 * 24 * 60 * 60) // 14 days
                        .rememberMeParameter("remember-me")
                        .rememberMeCookieName("placehub-remember-me")
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/login?logout=true")
                        .deleteCookies("placehub-remember-me")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());
        return http.build();
    }
}