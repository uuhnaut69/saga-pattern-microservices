package com.peppa.delivery.infrastructure.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peppa.delivery.domain.DeliveryUseCase;
import com.peppa.delivery.domain.PlacedDeliveryEvent;
import com.peppa.delivery.domain.port.EventHandlerPort;
import com.peppa.delivery.infrastructure.message.log.MessageLog;
import com.peppa.delivery.infrastructure.message.log.MessageLogRepository;
import com.peppa.delivery.infrastructure.message.outbox.Outbox;
import com.peppa.delivery.infrastructure.message.outbox.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventHandlerAdapter implements EventHandlerPort {
    ObjectMapper objectMapper;
    MessageLogRepository messageLogRepository;
    DeliveryUseCase deliveryUseCase;
    OutboxRepository outboxRepository;

    private static final String ORDER_CREATED = "ORDER_CREATED";
    private static final String DELIVERY_CREATED = "DELIVERY_CREATED";
    private static final String DELIVERY_CANCELLED = "DELIVERY_CANCELLED";
    private static final String ORDER = "ORDER";

    @Override
    @Transactional
    @Bean
    public Consumer<Message<String>> handleCreateDeliveryRequest() {
        return event -> {
            var id = event.getHeaders().getId();
            if (Objects.nonNull(id) && messageLogRepository.isMessageProcessed(id)) {
                var eventType = getHeaderAsString(event.getHeaders(), "evetType");
                if (eventType.equals(ORDER_CREATED)) {
                    PlacedDeliveryEvent placedDeliveryEvent = deserialize(event.getPayload());

                    log.info("Start processing delivery request ...");

                    var outbox = new Outbox();
                    outbox.setAggregateId(placedDeliveryEvent.id());
                    outbox.setAggregateType(ORDER);
                    outbox.setPayload(objectMapper.convertValue(placedDeliveryEvent, JsonNode.class));

                    if (deliveryUseCase.createDelivery(placedDeliveryEvent)) {
                        outbox.setType(DELIVERY_CREATED);
                    } else {
                        outbox.setType(DELIVERY_CANCELLED);
                    }
                    outboxRepository.save(outbox);
                    log.info("Done delivery request {}", placedDeliveryEvent);
                }
                messageLogRepository.save(new MessageLog(id, Timestamp.from(Instant.now())));
            }
        };
    }

    private String getHeaderAsString(MessageHeaders headers, String name) {
        var value = headers.get(name, byte[].class);
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("No header value for " + name);
        }
        return new String(value, StandardCharsets.UTF_8);
    }

    private PlacedDeliveryEvent deserialize(String event) {
        PlacedDeliveryEvent placedDeliveryEvent;
        try {
            placedDeliveryEvent = objectMapper.readValue(event, PlacedDeliveryEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Couldn't deserialize event", e);
        }
        return placedDeliveryEvent;
    }
}
