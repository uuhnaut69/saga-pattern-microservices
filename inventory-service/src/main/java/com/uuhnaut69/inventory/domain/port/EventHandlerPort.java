package com.uuhnaut69.inventory.domain.port;

import org.springframework.messaging.Message;

import java.util.function.Consumer;

public interface EventHandlerPort {

  Consumer<Message<String>> handleReserveProductStockRequest();
}
