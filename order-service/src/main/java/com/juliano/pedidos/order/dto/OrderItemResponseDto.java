package com.juliano.pedidos.order.dto;

import com.juliano.pedidos.order.model.Order;
import com.juliano.pedidos.product.model.Product;
import jakarta.persistence.*;

import java.math.BigDecimal;

public record OrderItemResponseDto (
        Long productId,
        String name,
        Integer quantity,
        BigDecimal unityPrice
){}
