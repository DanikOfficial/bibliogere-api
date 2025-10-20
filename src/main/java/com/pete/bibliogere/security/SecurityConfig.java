package com.pete.bibliogere.security;

import com.pete.bibliogere.security.filter.JwtExceptionHandlerFilter;
import com.pete.bibliogere.security.filter.JwtRequestFilter;
import com.pete.bibliogere.security.service.TokenProviderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfig(UserDetailsService userDetailsService,
                          AuthenticationEntryPoint authenticationEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter(UserDetailsService userDetailsService,
                                             TokenProviderService tokenProviderService) {
        return new JwtRequestFilter(userDetailsService, tokenProviderService);
    }

    @Bean
    public JwtExceptionHandlerFilter jwtExceptionHandlerFilter() {
        return new JwtExceptionHandlerFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtExceptionHandlerFilter jwtExceptionHandlerFilter,
                                                   JwtRequestFilter jwtRequestFilter) throws Exception {

        final String[] allowedMatchers = {
                "/",
                "/api/v1/utilizador/entrar",
                "/api/v1/utilizador/refresh",
                "/api/v1/localizacoes/**",
                "/api/v1/estantes/**",
                "/api/v1/questoes",
                "/api/v1/obras",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/webjars/**"
        };

        final String[] adminMatchers = {"/api/v1/admin/**"};
        final String[] adminAndAtendenteMatchers = {
                "/api/v1/obra/**",
                "/api/v1/obras/",
                "/api/v1/tipos",
                "/api/v1/utilizadores"
        };
        final String[] atendenteMatchers = {"/api/v1/atendente/**"};

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {}) // use CorsConfigurationSource bean if defined
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(allowedMatchers).permitAll()
                        .requestMatchers(adminMatchers).hasRole("ADMIN")
                        .requestMatchers(atendenteMatchers).hasRole("ATENDENTE")
                        .requestMatchers(adminAndAtendenteMatchers).hasAnyRole("ATENDENTE", "ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtExceptionHandlerFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
