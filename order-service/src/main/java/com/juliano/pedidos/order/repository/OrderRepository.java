package com.juliano.pedidos.order.repository;

import com.juliano.pedidos.order.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findOrderByUserId(Long userId, Pageable pageable);


}
