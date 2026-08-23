package com.juliano.pedidos.security.service;

import com.juliano.pedidos.security.model.UserPrincipal;
import com.juliano.pedidos.security.repository.UserRepository;
import com.juliano.pedidos.security.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String login){

        User user = userRepository.findByEmail(login)
                .or(() -> userRepository.findByUsername(login))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuário não encontrado: " + login));

        return new UserPrincipal(user);
    }
}
