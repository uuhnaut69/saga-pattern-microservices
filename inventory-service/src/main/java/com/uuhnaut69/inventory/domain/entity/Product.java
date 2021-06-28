package com.uuhnaut69.inventory.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

  private UUID id;

  private String name;

  private int stocks;
}
