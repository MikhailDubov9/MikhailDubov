package com.mipt.mikhaildubov.todo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class AuthConfig {

    @Value("${app.security.password-pepper}")
    private String pepper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return super.encode(rawPassword + pepper);
            }
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return super.matches(rawPassword + pepper, encodedPassword);
            }
        };
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails user = User.builder()
                .username("user")
                .password(encoder.encode("password"))
                .roles("USER")
                .build();

        UserDetails reader = User.builder()
                .username("reader")
                .password(encoder.encode("password"))
                .roles("USER")
                .authorities("READ_PRIVILEGE")
                .build();

        return new InMemoryUserDetailsManager(user, reader);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}