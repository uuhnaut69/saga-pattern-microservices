package com.uuhnaut69.order.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuhnaut69.common.PlacedOrderEvent;
import com.uuhnaut69.order.domain.OutBox;
import com.uuhnaut69.order.messagelog.MessageLogService;
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

import static com.uuhnaut69.common.AggregateType.ORDER;
import static com.uuhnaut69.common.EventType.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventHandler {

  private final ObjectMapper mapper;

  private final OrderService orderService;

  private final MessageLogService logService;

  private final OutBoxRepository outBoxRepository;

  @Bean
  @Transactional
  public Consumer<Message<String>> reserveCustomerBalanceStage() {
    return event -> {
      var messageId = event.getHeaders().getId();
      if (!logService.isProcessed(messageId)) {
        var placedOrderEvent = deserialize(event.getPayload());
        var eventType = getHeaderAsString(event.getHeaders(), "eventType");
        if (eventType.equals(RESERVE_CUSTOMER_BALANCE_SUCCESSFULLY.name())) {
          var outbox =
              OutBox.builder()
                  .aggregateId(placedOrderEvent.id())
                  .payload(mapper.convertValue(placedOrderEvent, JsonNode.class))
                  .build();
          outbox.setAggregateType(ORDER.name());
          outbox.setType(RESERVE_CUSTOMER_BALANCE_SUCCESSFULLY.name());
          outBoxRepository.save(outbox);
        } else if (eventType.equals(RESERVE_CUSTOMER_BALANCE_FAILED.name())) {
          orderService.updateOrderStatus(placedOrderEvent.id(), false);
        }
        logService.markAsProcessed(messageId);
      }
    };
  }

  @Bean
  @Transactional
  public Consumer<Message<String>> reserveProductStockStage() {
    return event -> {
      var messageId = event.getHeaders().getId();
      if (!logService.isProcessed(messageId)) {
        var placedOrderEvent = deserialize(event.getPayload());
        var eventType = getHeaderAsString(event.getHeaders(), "eventType");
        if (eventType.equals(RESERVE_PRODUCT_STOCK_SUCCESSFULLY.name())) {
          orderService.updateOrderStatus(placedOrderEvent.id(), true);
        } else if (eventType.equals(RESERVE_PRODUCT_STOCK_FAILED.name())) {
          orderService.updateOrderStatus(placedOrderEvent.id(), false);
          var outbox =
              OutBox.builder()
                  .aggregateId(placedOrderEvent.id())
                  .aggregateType(ORDER.name())
                  .type(COMPENSATE_CUSTOMER_BALANCE.name())
                  .payload(mapper.convertValue(placedOrderEvent, JsonNode.class))
                  .build();
          outBoxRepository.save(outbox);
        }
        logService.markAsProcessed(messageId);
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
      throw new IllegalArgumentException(
          String.format("Expected record header %s not present", name));
    }
    return new String(value, StandardCharsets.UTF_8);
  }
}
