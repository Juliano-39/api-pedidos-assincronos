package com.juliano.pedidos.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O email deve ser válido")
        String email,

        @NotBlank(message = "O nome de usuário é obrigatório")
        @Size(min = 2, max = 100, message = "O nome de usuário deve ter entre 2 e 100 caracteres")
        String username,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String password,

        String role // Opcional, ex: "ROLE_USER" ou "ROLE_ADMIN"
) {
}
