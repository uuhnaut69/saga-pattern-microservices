package com.uuhnaut69.order.infrastructure.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuhnaut69.order.domain.entity.Order;
import com.uuhnaut69.order.domain.port.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {

  private final ObjectMapper mapper;

  private final OrderJpaRepository orderJpaRepository;

  @Override
  public Optional<Order> findOrderById(UUID orderId) {
    return orderJpaRepository
        .findById(orderId)
        .map(orderEntity -> mapper.convertValue(orderEntity, Order.class));
  }

  @Override
  public void saveOrder(Order order) {
    var entity = mapper.convertValue(order, OrderEntity.class);
    orderJpaRepository.save(entity);
  }
}
