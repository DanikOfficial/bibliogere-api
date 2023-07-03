package com.pete.bibliogere.security.model;

import com.pete.bibliogere.modelo.Utilizador;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {

    private String username;

    private String password;

    private boolean enabled;

    private Collection<GrantedAuthority> authorities;

    public MyUserDetails(Utilizador utilizador) {
        this.username = utilizador.getUsername();
        this.password = utilizador.getPassword();
        this.enabled = utilizador.getEnabled();
        authorities = utilizador.getPermissoes()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getNome()))
                .collect(
                Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
