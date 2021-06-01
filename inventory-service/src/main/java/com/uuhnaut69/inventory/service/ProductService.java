package com.uuhnaut69.inventory.service;

import com.uuhnaut69.common.PlacedOrderEvent;
import com.uuhnaut69.inventory.api.request.ProductRequest;
import com.uuhnaut69.inventory.domain.Product;

import java.util.UUID;

public interface ProductService {

  Product findById(UUID productId);

  Product create(ProductRequest productRequest);

  void reserveProduct(PlacedOrderEvent orderEvent);
}
