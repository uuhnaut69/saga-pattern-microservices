package com.uuhnaut69.inventory.domain.port;

import java.util.function.Consumer;
import org.springframework.messaging.Message;

public interface EventHandlerPort {

  Consumer<Message<String>> handleReserveProductStockRequest();
}
