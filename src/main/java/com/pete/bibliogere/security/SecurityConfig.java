package com.pete.bibliogere.security;


import com.pete.bibliogere.security.filter.JwtExceptionHandlerFilter;
import com.pete.bibliogere.security.filter.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter();
    }

    @Autowired
    private JwtExceptionHandlerFilter jwtExceptionHandlerFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        final String allowedMathers[] = { "/api/v1/admin/**", "/api/v1/utilizador/entrar", "/api/v1/utilizador/refresh", "/api/v1/localizacoes/**", "/api/v1/estantes**", "/api/v1/questoes", };

        final String[] adminMatchers = {"/api/v1/adminw/**"};

        final String[] adminAndAtendenteMatchers = {"/api/v1/obra/**", "/api/v1/obras/",  "/api/v1/tipos"};

        final String[] atendenteMatchers = { "/api/v1/atendente/**"};


        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers(allowedMathers)
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(adminMatchers)
                .hasRole("ADMIN")
                .antMatchers(atendenteMatchers)
                .hasRole("ATENDENTE")
                .antMatchers(adminAndAtendenteMatchers)
                .hasAnyRole("ATENDENTE", "ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        // Enabled cors
        httpSecurity.cors();

        httpSecurity.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(jwtExceptionHandlerFilter, JwtRequestFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

}
