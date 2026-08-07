package com.thelastimperial.resmenu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final RememberMeServices rememberMeServices;

    public SecurityConfig(JwtAuthenticationConverter jwtAuthenticationConverter,
        RememberMeServices rememberMeServices
    ){
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
        this.rememberMeServices = rememberMeServices;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http){
        http
        .authorizeHttpRequests(auth->
            auth
            .requestMatchers("/resmenu/**").permitAll()
            .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers("/r/*").permitAll()
            .requestMatchers("/actuator/prometheus").hasRole("MONITOR")
            .anyRequest().authenticated()
        )
        .oauth2ResourceServer(oauth2 -> {
            oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter));
        })
        .rememberMe(rem -> 
            rem
            .rememberMeServices(rememberMeServices)
            .rememberMeParameter("remember-me")
        )
        .formLogin(
            login ->
                login
                .loginPage("/auth/login")
                .failureUrl("/auth/login?error=true")
                .defaultSuccessUrl("/", true)
                .permitAll()
        );
        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
