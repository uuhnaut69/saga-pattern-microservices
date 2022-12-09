package com.uuhnaut69.order.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuhnaut69.order.domain.entity.Order;
import com.uuhnaut69.order.domain.entity.OrderStatus;
import com.uuhnaut69.order.domain.port.OrderRepositoryPort;
import com.uuhnaut69.order.domain.port.OrderUseCasePort;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderUseCase implements OrderUseCasePort {

  private final ObjectMapper mapper;

  private final OrderRepositoryPort orderRepository;

  @Override
  public void placeOrder(OrderRequest orderRequest) {
    var order = mapper.convertValue(orderRequest, Order.class);
    order.setCreatedAt(Timestamp.from(Instant.now()));
    order.setStatus(OrderStatus.PENDING);
    order.setId(UUID.randomUUID());
    orderRepository.saveOrder(order);
    orderRepository.exportOutBoxEvent(order);
  }

  @Override
  public void updateOrderStatus(UUID orderId, boolean success) {
    var order = orderRepository.findOrderById(orderId);
    if (order.isPresent()) {
      if (success) {
        order.get().setStatus(OrderStatus.COMPLETED);
      } else {
        order.get().setStatus(OrderStatus.CANCELED);
      }
      orderRepository.saveOrder(order.get());
    }
  }
}
