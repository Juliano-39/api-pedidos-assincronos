package com.juliano.pedidos.order.dto;

import com.juliano.pedidos.order.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(

        Long id,
        Long userId,
        OrderStatus status,
        BigDecimal total,
        List<OrderItemResponseDto> products,
        LocalDateTime cratedAt,
        LocalDateTime updatedAt
) {}
