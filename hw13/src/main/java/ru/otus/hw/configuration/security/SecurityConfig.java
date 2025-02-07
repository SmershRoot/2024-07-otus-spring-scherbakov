package ru.otus.hw.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/", "/images/**", "/public/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/book").hasAnyRole("ADMIN", "EDITOR")
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling.accessDeniedHandler(accessDeniedHandler()))
                .rememberMe(rm -> rm.key("library-remember-me-key")
                        .tokenValiditySeconds(60))
        ;

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

}
