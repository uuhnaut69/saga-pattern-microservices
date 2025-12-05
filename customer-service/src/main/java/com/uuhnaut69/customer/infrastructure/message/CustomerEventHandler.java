package com.uuhnaut69.customer.infrastructure.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuhnaut69.customer.domain.PlacedOrderEvent;
import com.uuhnaut69.customer.domain.port.CustomerUseCasePort;
import com.uuhnaut69.customer.infrastructure.message.log.MessageLog;
import com.uuhnaut69.customer.infrastructure.message.log.MessageLogRepository;
import com.uuhnaut69.customer.infrastructure.message.outbox.OutBox;
import com.uuhnaut69.customer.infrastructure.message.outbox.OutBoxRepository;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerEventHandler {

  private final ObjectMapper mapper;
  private final CustomerUseCasePort customerUseCasePort;
  private final OutBoxRepository outBoxRepository;
  private final MessageLogRepository messageLogRepository;

  private static final String CUSTOMER = "CUSTOMER";
  private static final String ORDER_CREATED = "ORDER_CREATED";
  private static final String COMPENSATE_CUSTOMER_BALANCE = "COMPENSATE_CUSTOMER_BALANCE";
  private static final String RESERVE_CUSTOMER_BALANCE_FAILED = "RESERVE_CUSTOMER_BALANCE_FAILED";
  private static final String RESERVE_CUSTOMER_BALANCE_SUCCESSFULLY = "RESERVE_CUSTOMER_BALANCE_SUCCESSFULLY";

  @Transactional
  public void handleReserveCustomerBalanceRequest(Message<String> event) {
    var messageId = event.getHeaders().getId();
    if (Objects.nonNull(messageId) && !messageLogRepository.isMessageProcessed(messageId)) {
      var eventType = getHeaderAsString(event.getHeaders(), "eventType");
      if (eventType.equals(ORDER_CREATED)) {
        var placedOrderEvent = deserialize(event.getPayload());

        log.info("Start process reserve customer balance {}", placedOrderEvent);
        var outbox = new OutBox();
        outbox.setAggregateId(placedOrderEvent.id());
        outbox.setPayload(mapper.convertValue(placedOrderEvent, JsonNode.class));
        outbox.setAggregateType(CUSTOMER);

        if (customerUseCasePort.reserveBalance(placedOrderEvent)) {
          outbox.setType(RESERVE_CUSTOMER_BALANCE_SUCCESSFULLY);
        } else {
          outbox.setType(RESERVE_CUSTOMER_BALANCE_FAILED);
        }

        // Exported event into outbox table
        outBoxRepository.save(outbox);
        log.info("Done process reserve customer balance {}", placedOrderEvent);
      }
      // Marked message is processed
      messageLogRepository.save(new MessageLog(messageId, Timestamp.from(Instant.now())));
    }
  }

  @Transactional
  public void handleCompensateCustomerBalanceRequest(Message<String> event) {
    var messageId = event.getHeaders().getId();
    if (Objects.nonNull(messageId) && !messageLogRepository.existsById(messageId)) {
      var eventType = getHeaderAsString(event.getHeaders(), "eventType");
      if (eventType.equals(COMPENSATE_CUSTOMER_BALANCE)) {
        var placedOrderEvent = deserialize(event.getPayload());

        log.info("Start process compensate customer balance {}", placedOrderEvent);
        customerUseCasePort.compensateBalance(placedOrderEvent);
        log.info("Done process compensate customer balance {}", placedOrderEvent);
      }
      // Marked message is processed
      messageLogRepository.save(new MessageLog(messageId, Timestamp.from(Instant.now())));
    }
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
