package com.juliano.pedidos.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable()) // CSRF é desabilitado para APIs Stateless, será usado JWT
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**").permitAll() // Abertura de rotas para login e registro
            .requestMatchers("/admin/**").hasRole("ADMIN") // Rotas de administração só para usuários com papel ADMIN
            .anyRequest().authenticated() // Qualquer outra rota precisa de login ativo 
        );

    return http.build();
}

@Bean
public PasswordEncoder passwordEncoder() {
    // Faz com que as senhas sejam armazenadas de forma seguracom Hash BCrypt
    return new BCryptPasswordEncoder();
}

@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    // Objeto responsável por autenticar as credenciais do usuário, delegando para o UserDetailsService
    return config.getAuthenticationManager();
}

}
