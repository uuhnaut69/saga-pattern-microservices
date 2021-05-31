package com.uuhnaut69.customer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuhnaut69.common.PlacedOrderEvent;
import com.uuhnaut69.common.SagaStep;
import com.uuhnaut69.customer.api.exception.NotFoundException;
import com.uuhnaut69.customer.api.request.CustomerRequest;
import com.uuhnaut69.customer.domain.Customer;
import com.uuhnaut69.customer.domain.OutBox;
import com.uuhnaut69.customer.repository.CustomerRepository;
import com.uuhnaut69.customer.repository.OutBoxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static com.uuhnaut69.common.ReserveBalanceStatus.FAILED;
import static com.uuhnaut69.common.ReserveBalanceStatus.SUCCESSFUL;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

  private final ObjectMapper mapper;

  private final OutBoxRepository outBoxRepository;

  private final CustomerRepository customerRepository;

  @Override
  @Transactional(readOnly = true)
  public Customer findById(UUID customerId) {
    return customerRepository.findById(customerId).orElseThrow(NotFoundException::new);
  }

  @Override
  public Customer create(CustomerRequest customerRequest) {
    var customer = mapper.convertValue(customerRequest, Customer.class);
    customer.setBalance(BigDecimal.valueOf(10000));
    return customerRepository.save(customer);
  }

  @Override
  public void reserveBalance(PlacedOrderEvent orderEvent) {
    var customer = findById(orderEvent.customerId());
    var outbox = new OutBox();
    outbox.setAggregateId(orderEvent.id());
    outbox.setPayload(mapper.convertValue(orderEvent, JsonNode.class));
    outbox.setAggregateType(SagaStep.RESERVE_CUSTOMER_BALANCE_RESPONSE.name());

    if (customer
            .getBalance()
            .subtract(orderEvent.price().multiply(BigDecimal.valueOf(orderEvent.quantity())))
            .compareTo(BigDecimal.ZERO)
        < 0) {
      outbox.setType(FAILED.name());
    } else {
      customer.setBalance(
          customer
              .getBalance()
              .subtract(orderEvent.price().multiply(BigDecimal.valueOf(orderEvent.quantity()))));
      customerRepository.save(customer);
      outbox.setType(SUCCESSFUL.name());
    }
    outBoxRepository.save(outbox);
  }

  @Override
  public void compensateBalance(PlacedOrderEvent orderEvent) {
    var customer = findById(orderEvent.customerId());
    customer.setBalance(
        customer
            .getBalance()
            .add(orderEvent.price().multiply(BigDecimal.valueOf(orderEvent.quantity()))));
    customerRepository.save(customer);
  }
}
