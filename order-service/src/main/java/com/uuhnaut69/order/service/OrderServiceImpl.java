package com.uuhnaut69.order.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuhnaut69.order.api.request.OrderRequest;
import com.uuhnaut69.order.domain.Order;
import com.uuhnaut69.order.domain.OrderStatus;
import com.uuhnaut69.order.domain.OutBox;
import com.uuhnaut69.order.repository.OrderRepository;
import com.uuhnaut69.order.repository.OutBoxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private static final String ORDER = "ORDER";

  private static final String PLACED_ORDER_EVENT = "PLACED_ORDER_EVENT";

  private final OrderRepository orderRepository;

  private final OutBoxRepository outBoxRepository;

  @Override
  public void placeOrder(OrderRequest orderRequest) {
    var mapper =
        new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    var order = mapper.convertValue(orderRequest, Order.class);
    order.setCreatedAt(Timestamp.from(Instant.now()));
    order.setStatus(OrderStatus.PENDING);

    orderRepository.save(order);

    var outbox = new OutBox();
    outbox.setAggregateId(order.getId());
    outbox.setAggregateType(ORDER);
    outbox.setType(PLACED_ORDER_EVENT);
    var payload = mapper.convertValue(order, JsonNode.class);
    outbox.setPayload(payload);
    outBoxRepository.save(outbox);
  }
}
