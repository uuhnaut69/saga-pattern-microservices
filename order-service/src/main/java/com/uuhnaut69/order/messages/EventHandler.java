package com.uuhnaut69.order.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuhnaut69.common.PlacedOrderEvent;
import com.uuhnaut69.common.ReserveBalanceStatus;
import com.uuhnaut69.common.ReserveStockStatus;
import com.uuhnaut69.common.SagaStep;
import com.uuhnaut69.order.domain.OutBox;
import com.uuhnaut69.order.repository.OutBoxRepository;
import com.uuhnaut69.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Consumer;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class EventHandler {

  private final ObjectMapper mapper;

  private final OrderService orderService;

  private final OutBoxRepository outBoxRepository;

  @Bean
  public Consumer<Message<String>> startSaga() {
    return event -> {
      log.info("Start saga");
      var placedOrderEvent = deserialize(event.getPayload());
      var outbox =
          OutBox.builder()
              .aggregateType(SagaStep.RESERVE_CUSTOMER_BALANCE_REQUEST.name())
              .aggregateId(placedOrderEvent.id())
              .type(ReserveBalanceStatus.PENDING.name())
              .payload(mapper.convertValue(placedOrderEvent, JsonNode.class))
              .build();
      outBoxRepository.save(outbox);
    };
  }

  @Bean
  public Consumer<Message<String>> reserveCustomerBalanceStage() {
    return event -> {
      var placedOrderEvent = deserialize(event.getPayload());
      var eventType = getHeaderAsString(event.getHeaders(), "eventType");
      if (eventType.equals(ReserveBalanceStatus.SUCCESSFUL.name())) {
        var outbox =
            OutBox.builder()
                .aggregateId(placedOrderEvent.id())
                .payload(mapper.convertValue(placedOrderEvent, JsonNode.class))
                .build();
        outbox.setAggregateType(SagaStep.RESERVE_PRODUCT_STOCK_REQUEST.name());
        outbox.setType(ReserveStockStatus.PENDING.name());
        outBoxRepository.save(outbox);
      } else if (eventType.equals(ReserveBalanceStatus.FAILED.name())) {
        orderService.updateOrderStatus(placedOrderEvent.id(), false);
      }
    };
  }

  @Bean
  public Consumer<Message<String>> reserveProductStockStage() {
    return event -> {
      var placedOrderEvent = deserialize(event.getPayload());
      var eventType = getHeaderAsString(event.getHeaders(), "eventType");
      if (eventType.equals(ReserveStockStatus.SUCCESSFUL.name())) {
        orderService.updateOrderStatus(placedOrderEvent.id(), true);
      } else if (eventType.equals(ReserveStockStatus.FAILED.name())) {
        orderService.updateOrderStatus(placedOrderEvent.id(), false);
        var outbox =
            OutBox.builder()
                .aggregateId(placedOrderEvent.id())
                .aggregateType(SagaStep.COMPENSATE_CUSTOMER_BALANCE_REQUEST.name())
                .type(ReserveBalanceStatus.PENDING.name())
                .payload(mapper.convertValue(placedOrderEvent, JsonNode.class))
                .build();
        outBoxRepository.save(outbox);
      }
    };
  }

  private PlacedOrderEvent deserialize(String event) {
    PlacedOrderEvent placedOrderEvent;
    try {
      String unescaped = mapper.readValue(event, String.class);
      placedOrderEvent = mapper.readValue(unescaped, PlacedOrderEvent.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Couldn't deserialize event", e);
    }
    return placedOrderEvent;
  }

  private String getHeaderAsString(MessageHeaders headers, String name) {
    var value = headers.get(name, byte[].class);
    if (Objects.isNull(value)) {
      throw new IllegalArgumentException("Expected record header '" + name + "' not present");
    }
    return new String(value, StandardCharsets.UTF_8);
  }
}
