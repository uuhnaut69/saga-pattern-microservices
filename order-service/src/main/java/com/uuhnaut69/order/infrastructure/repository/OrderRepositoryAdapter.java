package com.uuhnaut69.order.infrastructure.repository;

import static com.uuhnaut69.order.infrastructure.message.EventHandlerAdapter.ORDER;
import static com.uuhnaut69.order.infrastructure.message.EventHandlerAdapter.ORDER_CREATED;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuhnaut69.order.domain.entity.Order;
import com.uuhnaut69.order.domain.port.OrderRepositoryPort;
import com.uuhnaut69.order.infrastructure.message.outbox.OutBox;
import com.uuhnaut69.order.infrastructure.message.outbox.OutBoxRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {

  private final ObjectMapper mapper;

  private final OrderJpaRepository orderJpaRepository;

  private final OutBoxRepository outBoxRepository;

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

  @Override
  public void exportOutBoxEvent(Order order) {
    var outbox =
        OutBox.builder()
            .aggregateId(order.getId())
            .aggregateType(ORDER)
            .type(ORDER_CREATED)
            .payload(mapper.convertValue(order, JsonNode.class))
            .build();
    outBoxRepository.save(outbox);
  }
}
