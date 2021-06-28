package com.uuhnaut69.inventory.api.request;

import javax.validation.constraints.NotBlank;

public record ProductRequest(@NotBlank String name, int stocks) {
}
