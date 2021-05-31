package com.uuhnaut69.inventory.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuhnaut69.common.PlacedOrderEvent;
import com.uuhnaut69.inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventHandler {

  private final ObjectMapper mapper;

  private final ProductService productService;

  @Bean
  public Consumer<Message<String>> handleReserveProductStockRequest() {
    return event -> {
      var placedOrderEvent = deserialize(event.getPayload());
      log.info("Start process reserve product stock {}", placedOrderEvent);
      productService.reserveProduct(placedOrderEvent);
      log.info("Done process reserve product stock {}", placedOrderEvent);
    };
  }

  @Bean
  public Consumer<Message<String>> handleCompensateProductStockRequest() {
    return event -> {
      var placedOrderEvent = deserialize(event.getPayload());
      log.info("Start process compensate product stock {}", placedOrderEvent);
      productService.compensateStock(placedOrderEvent);
      log.info("Done process compensate product stock {}", placedOrderEvent);
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
}
