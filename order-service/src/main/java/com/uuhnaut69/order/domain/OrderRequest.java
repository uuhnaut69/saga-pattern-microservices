package com.uuhnaut69.order.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record OrderRequest(@NotNull UUID customerId,
                           @NotNull UUID productId,
                           @Min(1) @NotNull Integer quantity,
                           BigDecimal price) {

}
