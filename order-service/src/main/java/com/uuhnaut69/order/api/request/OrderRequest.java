package com.uuhnaut69.order.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

  @NotNull private UUID customerId;

  @NotNull private UUID productId;

  @Min(1)
  @NotNull
  private Integer quantity;
}
