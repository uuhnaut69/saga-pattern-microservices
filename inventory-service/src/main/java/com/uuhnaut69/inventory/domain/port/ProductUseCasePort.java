package com.uuhnaut69.inventory.domain.port;

import com.uuhnaut69.inventory.domain.PlacedOrderEvent;
import com.uuhnaut69.inventory.domain.ProductRequest;
import com.uuhnaut69.inventory.domain.entity.Product;
import java.util.UUID;

public interface ProductUseCasePort {

  Product findById(UUID productId);

  Product create(ProductRequest productRequest);

  boolean reserveProduct(PlacedOrderEvent orderEvent);
}
