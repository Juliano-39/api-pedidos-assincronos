package com.juliano.pedidos.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(

    @NotBlank(message = "O nome de usuário é obrigatório")
    @Size(min = 2, max = 100, message = "O nome de usuário deve ter entre 2 e 100 caracteres")
    String username,

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    String password,

    String role // Opcional, ex: "ROLE_USER" ou "ROLE_ADMIN"
) {}
