package com.juliano.pedidos.order.dto;

import jakarta.validation.constraints.NotNull;

public record OrderItemRequestDto(

        @NotNull
        Long productId,

        Integer quantity
) {}
