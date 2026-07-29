package com.juliano.pedidos.security.controller;

import com.juliano.pedidos.security.model.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    /*
    Rota acessível por qualquer Role (ADMIN ou CUSTOMER),
    A anotation @AuthenticationPrincipal injeta o UserPrincipal que o
    JwtAuthenticationFilter colocou no SecutiryContextHolder.
     */
    @GetMapping("/me")
    public String me(@AuthenticationPrincipal UserPrincipal principal){
        return "Autenticado como: " + principal.getUsername();
    }

    // Rota acessível apenas para ADMIN, conforme definido no SecurityConfig
    // "/admin/**"
    @GetMapping("/admin/ping")
    public String adminPing(){
        return "Acesso admin confirmado.";
    }
}
