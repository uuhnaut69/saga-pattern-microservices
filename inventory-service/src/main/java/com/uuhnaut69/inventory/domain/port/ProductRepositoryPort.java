package com.uuhnaut69.inventory.domain.port;

import com.uuhnaut69.inventory.domain.entity.Product;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepositoryPort {

  Optional<Product> findProductById(UUID productId);

  Product saveProduct(Product product);
}
