package com.uuhnaut69.inventory.domain.entity;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

  private UUID id;

  private String name;

  private int stocks;
}
