package com.uuhnaut69.customer.domain.port;

import com.uuhnaut69.customer.domain.entity.Customer;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepositoryPort {

  Optional<Customer> findCustomerById(UUID customerId);

  Customer saveCustomer(Customer customer);
}
