package com.uuhnaut69.order.infrastructure.message;

import com.uuhnaut69.order.domain.port.EventHandlerPort;
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

  private final OrderEventHandler orderEventHandler;

  public static final String ORDER = "ORDER";

  public static final String ORDER_CREATED = "ORDER_CREATED";

  @Bean
  @Override
  public Consumer<Message<String>> reserveCustomerBalanceStage() {
    return orderEventHandler::handleReserveCustomerBalance;
  }

  @Bean
  @Override
  public Consumer<Message<String>> reserveProductStockStage() {
    return orderEventHandler::handleReserveProductStock;
  }
}
