package com.juliano.pedidos.security.service;

import com.juliano.pedidos.security.dto.LoginRequestDTO;
import com.juliano.pedidos.security.dto.LoginResponseDTO;
import com.juliano.pedidos.security.dto.RegisterRequestDTO;
import com.juliano.pedidos.security.model.Role;
import com.juliano.pedidos.security.model.UserPrincipal;
import com.juliano.pedidos.security.repository.UserRepository;
import com.juliano.pedidos.security.model.User;
import com.juliano.pedidos.shared.exception.DuplicateResourceException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequestDTO request){
        if (userRepository.findByUsername(request.username()).isPresent()){
            throw new DuplicateResourceException("Nome de usuário já está em uso");
        }

        User user = new User();
        user.setUsername(request.username());
        // Nunca salvar senha em texto puro — o BCryptPasswordEncoder
        // gera um hash diferente a cada chamada, mesmo pra senhas iguais,
        // graças ao "salt" embutido automaticamente por ele.
        user.setPassword(passwordEncoder.encode(request.password()));
        // Todo cadastro publico ascerá como CUSTOMER,
        // independente do que venha na requisição
        // Perfis ADMIN serão atribuídos por outro fluxo
        user.setRole(Role.CUSTOMER);

        userRepository.save(user);
    }

    public LoginResponseDTO login(LoginRequestDTO request){
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        String token = jwtService.generateToken(userPrincipal);

        return new LoginResponseDTO(token, userPrincipal.getUsername());
    }
}
