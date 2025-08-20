package com.peppa.delivery.domain.port;

import org.springframework.messaging.Message;

import java.util.function.Consumer;

public interface EventHandlerPort {

    Consumer<Message<String>> handleCreateDeliveryRequest();
}
