package com.uuhnaut69.customer.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuhnaut69.customer.domain.entity.Customer;
import com.uuhnaut69.customer.domain.exception.NotFoundException;
import com.uuhnaut69.customer.domain.port.CustomerRepositoryPort;
import com.uuhnaut69.customer.domain.port.CustomerUseCasePort;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerUseCase implements CustomerUseCasePort {

  private final ObjectMapper mapper;

  private final CustomerRepositoryPort customerRepository;

  @Override
  public Customer findById(UUID customerId) {
    return customerRepository.findCustomerById(customerId).orElseThrow(NotFoundException::new);
  }

  @Override
  public Customer create(CustomerRequest customerRequest) {
    var customer = mapper.convertValue(customerRequest, Customer.class);
    customer.setId(UUID.randomUUID());
    return customerRepository.saveCustomer(customer);
  }

  @Override
  public boolean reserveBalance(PlacedOrderEvent orderEvent) {
    var customer = findById(orderEvent.customerId());
    if (customer
        .getBalance()
        .subtract(orderEvent.price().multiply(BigDecimal.valueOf(orderEvent.quantity())))
        .compareTo(BigDecimal.ZERO)
        < 0) {
      return false;
    }
    customer.setBalance(
        customer
            .getBalance()
            .subtract(orderEvent.price().multiply(BigDecimal.valueOf(orderEvent.quantity()))));
    customerRepository.saveCustomer(customer);
    return true;
  }

  @Override
  public void compensateBalance(PlacedOrderEvent orderEvent) {
    var customer = findById(orderEvent.customerId());
    customer.setBalance(
        customer
            .getBalance()
            .add(orderEvent.price().multiply(BigDecimal.valueOf(orderEvent.quantity()))));
    customerRepository.saveCustomer(customer);
  }
}
