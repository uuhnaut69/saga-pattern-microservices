package com.uuhnaut69.customer.infrastructure.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuhnaut69.customer.domain.entity.Customer;
import com.uuhnaut69.customer.domain.port.CustomerRepositoryPort;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepositoryPort {

  private final ObjectMapper mapper;

  private final CustomerJpaRepository customerJpaRepository;

  @Override
  public Optional<Customer> findCustomerById(UUID customerId) {
    var entity = customerJpaRepository.findById(customerId);
    return entity.map(customerEntity -> mapper.convertValue(customerEntity, Customer.class));
  }

  @Override
  public Customer saveCustomer(Customer customer) {
    var entity = mapper.convertValue(customer, CustomerEntity.class);
    customerJpaRepository.save(entity);
    return customer;
  }
}
