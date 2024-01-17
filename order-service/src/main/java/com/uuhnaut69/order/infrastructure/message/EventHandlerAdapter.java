package com.uuhnaut69.order.infrastructure.message;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuhnaut69.order.domain.PlacedOrderEvent;
import com.uuhnaut69.order.domain.port.EventHandlerPort;
import com.uuhnaut69.order.domain.port.OrderUseCasePort;
import com.uuhnaut69.order.infrastructure.message.log.MessageLog;
import com.uuhnaut69.order.infrastructure.message.log.MessageLogRepository;
import com.uuhnaut69.order.infrastructure.message.outbox.OutBox;
import com.uuhnaut69.order.infrastructure.message.outbox.OutBoxRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventHandlerAdapter implements EventHandlerPort {

  private final ObjectMapper mapper;

  private final OrderUseCasePort orderUseCase;

  private final MessageLogRepository messageLogRepository;

  private final OutBoxRepository outBoxRepository;

  public static final String ORDER = "ORDER";

  public static final String ORDER_CREATED = "ORDER_CREATED";

  private static final String RESERVE_CUSTOMER_BALANCE_SUCCESSFULLY = "RESERVE_CUSTOMER_BALANCE_SUCCESSFULLY";

  private static final String RESERVE_CUSTOMER_BALANCE_FAILED = "RESERVE_CUSTOMER_BALANCE_FAILED";

  private static final String RESERVE_PRODUCT_STOCK_SUCCESSFULLY = "RESERVE_PRODUCT_STOCK_SUCCESSFULLY";

  private static final String RESERVE_PRODUCT_STOCK_FAILED = "RESERVE_PRODUCT_STOCK_FAILED";

  private static final String COMPENSATE_CUSTOMER_BALANCE = "COMPENSATE_CUSTOMER_BALANCE";

  @Bean
  @Override
  @Transactional
  public Consumer<Message<String>> reserveCustomerBalanceStage() {
    return event -> {
      var messageId = event.getHeaders().getId();
      if (Objects.nonNull(messageId) && !messageLogRepository.isMessageProcessed(messageId)) {
        var placedOrderEvent = deserialize(event.getPayload());
        var eventType = getHeaderAsString(event.getHeaders(), "eventType");
        if (eventType.equals(RESERVE_CUSTOMER_BALANCE_SUCCESSFULLY)) {
          var outbox = OutBox.builder()
              .aggregateId(placedOrderEvent.id())
              .payload(mapper.convertValue(placedOrderEvent, JsonNode.class))
              .aggregateType(ORDER)
              .type(RESERVE_CUSTOMER_BALANCE_SUCCESSFULLY)
              .build();
          outBoxRepository.save(outbox);
        } else if (eventType.equals(RESERVE_CUSTOMER_BALANCE_FAILED)) {
          orderUseCase.updateOrderStatus(placedOrderEvent.id(), false);
        }

        // Marked message is processed
        messageLogRepository.save(new MessageLog(messageId, Timestamp.from(Instant.now())));
      }
    };
  }

  @Bean
  @Override
  @Transactional
  public Consumer<Message<String>> reserveProductStockStage() {
    return event -> {
      var messageId = event.getHeaders().getId();
      if (Objects.nonNull(messageId) && !messageLogRepository.existsById(messageId)) {
        var placedOrderEvent = deserialize(event.getPayload());
        var eventType = getHeaderAsString(event.getHeaders(), "eventType");
        if (eventType.equals(RESERVE_PRODUCT_STOCK_SUCCESSFULLY)) {
          orderUseCase.updateOrderStatus(placedOrderEvent.id(), true);
        } else if (eventType.equals(RESERVE_PRODUCT_STOCK_FAILED)) {
          orderUseCase.updateOrderStatus(placedOrderEvent.id(), false);
          var outbox = OutBox.builder()
              .aggregateId(placedOrderEvent.id())
              .aggregateType(ORDER)
              .type(COMPENSATE_CUSTOMER_BALANCE)
              .payload(mapper.convertValue(placedOrderEvent, JsonNode.class))
              .build();
          outBoxRepository.save(outbox);
        }

        // Marked message is processed
        messageLogRepository.save(new MessageLog(messageId, Timestamp.from(Instant.now())));
      }
    };
  }

  private PlacedOrderEvent deserialize(String event) {
    PlacedOrderEvent placedOrderEvent;
    try {
      placedOrderEvent = mapper.readValue(event, PlacedOrderEvent.class);
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
