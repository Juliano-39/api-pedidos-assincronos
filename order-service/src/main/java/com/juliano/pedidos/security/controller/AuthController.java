package com.juliano.pedidos.security.controller;

import com.juliano.pedidos.security.dto.LoginRequestDTO;
import com.juliano.pedidos.security.dto.LoginResponseDTO;
import com.juliano.pedidos.security.dto.RegisterRequestDTO;
import com.juliano.pedidos.security.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.juliano.pedidos.security.service.JwtService;
import com.juliano.pedidos.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDTO request) {
        // @Valid ativa as anotações do RegisterRequestDTO (@NotBlank, @Size).
        // Se algum campo vier inválido, o Spring já responde 400 automaticamente,
        // antes mesmo desse método ser executado.
        authService.register(request);

        // 201 Created é o status semanticamente correto para "recurso criado
        // com sucesso" — mais preciso que devolver 200 aqui.
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    



}
