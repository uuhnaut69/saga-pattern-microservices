package com.uuhnaut69.customer.api;

import com.uuhnaut69.customer.api.request.CustomerRequest;
import com.uuhnaut69.customer.domain.Customer;
import com.uuhnaut69.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

  private final CustomerService customerService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Customer create(@RequestBody @Valid CustomerRequest customerRequest) {
    return customerService.create(customerRequest);
  }

  @GetMapping("/{customerId}")
  public Customer findById(@PathVariable UUID customerId) {
    return customerService.findById(customerId);
  }
}
