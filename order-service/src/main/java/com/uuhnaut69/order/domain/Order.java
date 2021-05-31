package com.uuhnaut69.order.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "orders")
public class Order {

  @Id @GeneratedValue private UUID id;

  @Column(nullable = false)
  private UUID customerId;

  @Column(nullable = false)
  private UUID productId;

  private BigDecimal price;

  private int quantity;

  private Timestamp createdAt;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private OrderStatus status;
}
