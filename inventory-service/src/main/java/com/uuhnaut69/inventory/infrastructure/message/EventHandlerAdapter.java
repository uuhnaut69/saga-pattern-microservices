package com.uuhnaut69.inventory.infrastructure.message;

import com.uuhnaut69.inventory.domain.port.EventHandlerPort;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventHandlerAdapter implements EventHandlerPort {

  private final InventoryEventHandler inventoryEventHandler;

  @Bean
  @Override
  public Consumer<Message<String>> handleReserveProductStockRequest() {
    return inventoryEventHandler::handleReserveProductStockRequest;
  }
}
