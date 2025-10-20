package com.pete.bibliogere.security.service.impl;

import com.pete.bibliogere.modelo.Utilizador;
import com.pete.bibliogere.repositorios.UtilizadorRepositorio;
import com.pete.bibliogere.security.model.MyUserDetails;
import com.pete.bibliogere.services.UtilizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UtilizadorRepositorio utilizadorRepository;

    public UserDetailsServiceImpl(UtilizadorRepositorio utilizadorRepository) {
        this.utilizadorRepository = utilizadorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = utilizadorRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new MyUserDetails(user);
    }
}

