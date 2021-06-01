package com.uuhnaut69.customer.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuhnaut69.common.PlacedOrderEvent;
import com.uuhnaut69.customer.messagelog.MessageLogService;
import com.uuhnaut69.customer.service.CustomerService;
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

import static com.uuhnaut69.common.EventType.COMPENSATE_CUSTOMER_BALANCE;
import static com.uuhnaut69.common.EventType.ORDER_CREATED;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventHandler {

  private final ObjectMapper mapper;

  private final MessageLogService logService;

  private final CustomerService customerService;

  @Bean
  @Transactional
  public Consumer<Message<String>> handleReserveCustomerBalanceRequest() {
    return event -> {
      var messageId = event.getHeaders().getId();
      if (!logService.isProcessed(messageId)) {
        var eventType = getHeaderAsString(event.getHeaders(), "eventType");
        if (eventType.equals(ORDER_CREATED.name())) {
          var placedOrderEvent = deserialize(event.getPayload());
          log.info("Start process reserve customer balance {}", placedOrderEvent);
          customerService.reserveBalance(placedOrderEvent);
          log.info("Done process reserve customer balance {}", placedOrderEvent);
        }
        logService.markAsProcessed(messageId);
      }
    };
  }

  @Bean
  @Transactional
  public Consumer<Message<String>> handleCompensateCustomerBalanceRequest() {
    return event -> {
      var messageId = event.getHeaders().getId();
      if (!logService.isProcessed(messageId)) {
        var eventType = getHeaderAsString(event.getHeaders(), "eventType");
        if (eventType.equals(COMPENSATE_CUSTOMER_BALANCE.name())) {
          var placedOrderEvent = deserialize(event.getPayload());
          log.info("Start process compensate customer balance {}", placedOrderEvent);
          customerService.compensateBalance(placedOrderEvent);
          log.info("Done process compensate customer balance {}", placedOrderEvent);
        }
        logService.markAsProcessed(messageId);
      }
    };
  }

  private PlacedOrderEvent deserialize(String event) {
    PlacedOrderEvent placedOrderEvent = null;
    try {
      String unescaped = mapper.readValue(event, String.class);
      placedOrderEvent = mapper.readValue(unescaped, PlacedOrderEvent.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
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
