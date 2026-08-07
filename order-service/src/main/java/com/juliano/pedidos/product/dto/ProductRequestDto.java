package com.juliano.pedidos.product.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequestDto(

        @NotBlank
        @Size(max = 100)
        String name,

        @Size(min = 10, max = 200)
        String description,

        @NotNull
        @Positive
        BigDecimal price,

        @PositiveOrZero
        Integer quantity,

        boolean status,

        @NotNull
        Long categoryId
) {}
