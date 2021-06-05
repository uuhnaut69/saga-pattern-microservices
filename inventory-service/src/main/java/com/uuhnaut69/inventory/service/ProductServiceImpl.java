package com.uuhnaut69.inventory.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuhnaut69.common.event.PlacedOrderEvent;
import com.uuhnaut69.common.exception.NotFoundException;
import com.uuhnaut69.inventory.api.request.ProductRequest;
import com.uuhnaut69.inventory.domain.OutBox;
import com.uuhnaut69.inventory.domain.Product;
import com.uuhnaut69.inventory.repository.OutBoxRepository;
import com.uuhnaut69.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.uuhnaut69.common.event.AggregateType.PRODUCT;
import static com.uuhnaut69.common.event.EventType.RESERVE_PRODUCT_STOCK_FAILED;
import static com.uuhnaut69.common.event.EventType.RESERVE_PRODUCT_STOCK_SUCCESSFULLY;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ObjectMapper mapper;

  private final OutBoxRepository outBoxRepository;

  private final ProductRepository productRepository;

  @Override
  public Product findById(UUID productId) {
    return productRepository.findById(productId).orElseThrow(NotFoundException::new);
  }

  @Override
  public Product create(ProductRequest productRequest) {
    var product = mapper.convertValue(productRequest, Product.class);
    return productRepository.save(product);
  }

  @Override
  public void reserveProduct(PlacedOrderEvent orderEvent) {
    var product = findById(orderEvent.productId());
    var outbox = new OutBox();
    outbox.setAggregateId(orderEvent.id());
    outbox.setAggregateType(PRODUCT.name());
    outbox.setPayload(mapper.convertValue(orderEvent, JsonNode.class));

    if (product.getStocks() - orderEvent.quantity() < 0) {
      outbox.setType(RESERVE_PRODUCT_STOCK_FAILED.name());
    } else {
      product.setStocks(product.getStocks() - orderEvent.quantity());
      productRepository.save(product);
      outbox.setType(RESERVE_PRODUCT_STOCK_SUCCESSFULLY.name());
    }
    outBoxRepository.save(outbox);
  }
}
