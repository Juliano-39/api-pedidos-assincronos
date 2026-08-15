package com.juliano.pedidos.order.controller;

import com.juliano.pedidos.order.dto.OrderRequestDto;
import com.juliano.pedidos.order.dto.OrderResponseDto;
import com.juliano.pedidos.order.service.OrderService;
import com.juliano.pedidos.security.model.UserPrincipal;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> create(
            @AuthenticationPrincipal UserPrincipal authenticatedUser,
            @Valid @RequestBody OrderRequestDto request){

        OrderResponseDto response = orderService.create
                (authenticatedUser.getUser(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<OrderResponseDto> pay(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserPrincipal authenticatedUser){

        OrderResponseDto response = orderService.pay
                (orderId, authenticatedUser.getUser());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponseDto> cancel(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserPrincipal authenticatedUser){

        OrderResponseDto response = orderService.cancel(orderId, authenticatedUser.getUser());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getById(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserPrincipal authenticatedUser){

        OrderResponseDto response = orderService.listOrderById(orderId, authenticatedUser.getUser());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDto>> listAll(
            @AuthenticationPrincipal UserPrincipal authenticatedUser,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable){

        return ResponseEntity.ok(orderService.listAllOrders(authenticatedUser.getUser(), pageable));
    }
}
