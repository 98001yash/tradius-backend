package com.company.tradius_backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class UserDetailsConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        // We use JWT, not UserDetailsService
        return username -> {
            throw new UnsupportedOperationException(
                    "UserDetailsService is not used. JWT authentication only."
            );
        };
    }
}
