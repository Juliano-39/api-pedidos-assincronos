package com.juliano.pedidos.order.service;

import com.juliano.pedidos.order.dto.OrderItemRequestDto;
import com.juliano.pedidos.order.dto.OrderItemResponseDto;
import com.juliano.pedidos.order.dto.OrderRequestDto;
import com.juliano.pedidos.order.dto.OrderResponseDto;
import com.juliano.pedidos.order.model.Order;
import com.juliano.pedidos.order.model.OrderItem;
import com.juliano.pedidos.order.model.OrderStatus;
import com.juliano.pedidos.order.repository.OrderRepository;
import com.juliano.pedidos.product.model.Product;
import com.juliano.pedidos.product.repository.ProductRepository;
import com.juliano.pedidos.security.model.Role;
import com.juliano.pedidos.security.model.User;
import com.juliano.pedidos.security.repository.UserRepository;
import com.juliano.pedidos.shared.exception.InsufficientStockException;
import com.juliano.pedidos.shared.exception.InvalidOrderStateException;
import com.juliano.pedidos.shared.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository){
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public OrderResponseDto create(User authenticatedUser, OrderRequestDto request){
        // Nota: Método incompleto.
        //  Para implementação futura, incluir reserva de estoque
        //  para evitar concorrÊncia entre pedidos

        Order order = new Order();
        order.setUser(authenticatedUser);
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequestDto itemRequest :request.items()){
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

            if(itemRequest.quantity() > product.getQuantity()){
                throw new InsufficientStockException("Estoque insuficiente");
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setProductName(product.getName());
            item.setUnitPrice(product.getPrice());
            item.setQuantity(itemRequest.quantity());

            BigDecimal itemTotal = item.getUnitPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            total = total.add(itemTotal);

            items.add(item);
        }

        order.setItems(items);
        order.setTotal(total);

        orderRepository.save(order);

        return toResponseDto(order);
    }

    @Transactional
    public OrderResponseDto pay(Long orderId, User authenticatedUser){
        // Nota: atualmente é o método que modifica o estoque dos produtos,
        //  futuramente poderá modificar um estoque paralelo
        //  que contenha os produtos em espera.

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        if (!order.getUser().getId().equals(authenticatedUser.getId())){
            throw new AccessDeniedException("Acesso negado!");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStateException("Pedido não está com pagamento pendente");
        }

        for (OrderItem item: order.getItems()) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        return toResponseDto(order);
    }

    @Transactional
    public OrderResponseDto cancel(Long orderId, User authenticatedUser){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStateException("Pedido não está com pagamento pendente");
        }

        boolean isAdmin = authenticatedUser.getRole() == Role.ADMIN;
        boolean isOwner = order.getUser().getId().equals(authenticatedUser.getId());

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Cancelamento negado");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        return toResponseDto(order);
    }

    public OrderResponseDto listOrderById(Long orderId, User authenticatedUser) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        boolean isAdmin = authenticatedUser.getRole() == Role.ADMIN;
        boolean isOwner = order.getUser().getId().equals(authenticatedUser.getId());

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Acesso negado");
        }

        return toResponseDto(order);
    }


    public Page<OrderResponseDto> listAllOrders(User authenticatedUser, Pageable pageable) {

        Page<Order> orders;

        if (authenticatedUser.getRole() == Role.ADMIN) {
            orders = orderRepository.findAll(pageable);
        } else {
            orders = orderRepository.findOrderByUserId(authenticatedUser.getId(), pageable);
        }

        return orders.map(this::toResponseDto);
    }

    private OrderResponseDto toResponseDto(Order order){

        List<OrderItemResponseDto> itemResponse = order.getItems()
                .stream()
                .map(item -> new OrderItemResponseDto(
                        item.getProduct().getId(),
                        item.getProductName(),
                        item.getQuantity(),
                        item.getUnitPrice()
                ))
                .toList();

        return new OrderResponseDto(
                order.getId(),
                order.getUser().getId(),
                order.getStatus(),
                order.getTotal(),
                itemResponse,
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
