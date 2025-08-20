package com.peppa.delivery.domain;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PlacedDeliveryEvent(
        @NotNull UUID id,

        @NotNull
        String address) {
}
