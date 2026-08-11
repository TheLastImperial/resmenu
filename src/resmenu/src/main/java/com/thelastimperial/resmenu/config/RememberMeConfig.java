package com.thelastimperial.resmenu.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
public class RememberMeConfig {

    @Bean
    public RememberMeServices rememberMeServices(
        @Value("${com.thelastimperial.resmenu.rememberme.key}") String key,
        UserDetailsService userDetailsService, PersistentTokenRepository persistentTokenRepository
    ){
        return new PersistentTokenBasedRememberMeServices(
            key, userDetailsService, persistentTokenRepository
        );
    }

    @Bean
    public JdbcTokenRepositoryImpl getJdbcTokenRepositoryImpl(DataSource dataSource){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }
}

