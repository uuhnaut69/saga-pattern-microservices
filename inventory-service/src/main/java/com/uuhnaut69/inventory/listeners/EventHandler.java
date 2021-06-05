package com.uuhnaut69.inventory.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuhnaut69.common.event.PlacedOrderEvent;
import com.uuhnaut69.inventory.messagelog.MessageLogService;
import com.uuhnaut69.inventory.service.ProductService;
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

import static com.uuhnaut69.common.event.EventType.RESERVE_CUSTOMER_BALANCE_SUCCESSFULLY;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventHandler {

  private final ObjectMapper mapper;

  private final MessageLogService logService;

  private final ProductService productService;

  @Bean
  @Transactional
  public Consumer<Message<String>> handleReserveProductStockRequest() {
    return event -> {
      var messageId = event.getHeaders().getId();
      if (!logService.isProcessed(messageId)) {
        var eventType = getHeaderAsString(event.getHeaders(), "eventType");
        if (eventType.equals(RESERVE_CUSTOMER_BALANCE_SUCCESSFULLY.name())) {
          var placedOrderEvent = deserialize(event.getPayload());
          log.info("Start process reserve product stock {}", placedOrderEvent);
          productService.reserveProduct(placedOrderEvent);
          log.info("Done process reserve product stock {}", placedOrderEvent);
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
