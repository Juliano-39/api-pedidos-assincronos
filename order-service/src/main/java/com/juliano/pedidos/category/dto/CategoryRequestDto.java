package com.juliano.pedidos.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.lang.NonNull;

public record CategoryRequestDto(

        @NotBlank(message = "Nome obrigatório")
        @Size(min = 6, max = 20)
        String name,

        @Size(max = 200)
        String description
) {
}
