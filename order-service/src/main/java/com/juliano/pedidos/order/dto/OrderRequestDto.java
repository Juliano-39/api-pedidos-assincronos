package com.juliano.pedidos.order.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderRequestDto(

        @NotEmpty
        List<OrderItemRequestDto> items
) {}
