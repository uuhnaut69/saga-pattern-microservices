package com.uuhnaut69.customer.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuhnaut69.common.PlacedOrderEvent;
import com.uuhnaut69.customer.service.CustomerService;
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

  private final CustomerService customerService;

  @Bean
  public Consumer<Message<String>> handleReserveCustomerBalanceRequest() {
    return event -> {
      var placedOrderEvent = deserialize(event.getPayload());
      log.info("Start process reserve customer balance {}", placedOrderEvent);
      customerService.reserveBalance(placedOrderEvent);
      log.info("Done process reserve customer balance {}", placedOrderEvent);
    };
  }

  @Bean
  public Consumer<Message<String>> handleCompensateCustomerBalanceRequest() {
    return event -> {
      var placedOrderEvent = deserialize(event.getPayload());
      log.info("Start process compensate customer balance {}", placedOrderEvent);
      customerService.compensateBalance(placedOrderEvent);
      log.info("Done process compensate customer balance {}", placedOrderEvent);
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
