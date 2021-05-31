package com.uuhnaut69.order.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuhnaut69.common.SagaStep;
import com.uuhnaut69.order.api.request.OrderRequest;
import com.uuhnaut69.order.domain.Order;
import com.uuhnaut69.order.domain.OrderStatus;
import com.uuhnaut69.order.domain.OutBox;
import com.uuhnaut69.order.repository.OrderRepository;
import com.uuhnaut69.order.repository.OutBoxRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final ObjectMapper mapper;

  private final OrderRepository orderRepository;

  private final OutBoxRepository outBoxRepository;

  private static final String SUCCESSFUL = "SUCCESSFUL";

  @Override
  @SneakyThrows
  public void placeOrder(OrderRequest orderRequest) {
    var order = mapper.convertValue(orderRequest, Order.class);
    order.setCreatedAt(Timestamp.from(Instant.now()));
    order.setStatus(OrderStatus.PENDING);
    orderRepository.save(order);

    var outbox =
        OutBox.builder()
            .aggregateId(order.getId())
            .aggregateType(SagaStep.PLACE_ORDER_REQUEST.name())
            .type(SUCCESSFUL)
            .payload(mapper.convertValue(order, JsonNode.class))
            .build();
    outBoxRepository.save(outbox);
    log.info("Persisted order event {} to out_box table", outbox);
  }

  @Override
  public void updateOrderStatus(UUID orderId, boolean success) {
    var order = orderRepository.findById(orderId);
    if (order.isPresent()) {
      if (success) {
        order.get().setStatus(OrderStatus.COMPLETED);
      } else {
        order.get().setStatus(OrderStatus.CANCELED);
      }
      orderRepository.save(order.get());
    }
  }
}
