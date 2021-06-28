package com.uuhnaut69.order.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

  private UUID id;

  private UUID customerId;

  private UUID productId;

  private BigDecimal price;

  private int quantity;

  private Timestamp createdAt;

  private OrderStatus status;
}
