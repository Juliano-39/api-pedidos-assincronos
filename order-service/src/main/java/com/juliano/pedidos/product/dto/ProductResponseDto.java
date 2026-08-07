package com.juliano.pedidos.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponseDto(

        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer quantity,
        boolean status,
        Long categoryId,
        String categoryName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
