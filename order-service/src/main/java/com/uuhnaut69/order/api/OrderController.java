package com.uuhnaut69.order.api;

import com.uuhnaut69.order.domain.OrderRequest;
import com.uuhnaut69.order.domain.port.OrderUseCasePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

  private final OrderUseCasePort orderUseCase;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void placeOrder(@RequestBody @Valid OrderRequest orderRequest) {
    log.info("Received new order request {}", orderRequest);
    orderUseCase.placeOrder(orderRequest);
  }
}
