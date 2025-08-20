package com.peppa.delivery.domain;

import jakarta.validation.constraints.NotNull;

public record DeliveryRequest(@NotNull String address) {
}
