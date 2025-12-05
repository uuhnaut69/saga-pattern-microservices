package com.uuhnaut69.customer.infrastructure.message;

import com.uuhnaut69.customer.domain.port.EventHandlerPort;
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

  private final CustomerEventHandler customerEventHandler;

  @Bean
  @Override
  public Consumer<Message<String>> handleReserveCustomerBalanceRequest() {
    return customerEventHandler::handleReserveCustomerBalanceRequest;
  }

  @Bean
  @Override
  public Consumer<Message<String>> handleCompensateCustomerBalanceRequest() {
    return customerEventHandler::handleCompensateCustomerBalanceRequest;
  }
}
