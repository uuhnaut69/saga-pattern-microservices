package com.uuhnaut69.customer.domain.entity;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

  private UUID id;

  private String username;

  private String fullName;

  private BigDecimal balance;
}
