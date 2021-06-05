package com.uuhnaut69.customer.service;

import com.uuhnaut69.common.event.PlacedOrderEvent;
import com.uuhnaut69.customer.api.request.CustomerRequest;
import com.uuhnaut69.customer.domain.Customer;

import java.util.UUID;

public interface CustomerService {

  Customer findById(UUID customerId);

  Customer create(CustomerRequest customerRequest);

  void reserveBalance(PlacedOrderEvent orderEvent);

  void compensateBalance(PlacedOrderEvent orderEvent);
}
