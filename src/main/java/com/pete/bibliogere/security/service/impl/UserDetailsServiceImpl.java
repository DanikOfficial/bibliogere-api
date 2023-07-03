package com.pete.bibliogere.security.service.impl;

import com.pete.bibliogere.modelo.Utilizador;
import com.pete.bibliogere.security.model.MyUserDetails;
import com.pete.bibliogere.services.UtilizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UtilizadorService service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Utilizador utilizador = service.pesquisarPorUsername(username);

        return new MyUserDetails(utilizador);
    }
}
