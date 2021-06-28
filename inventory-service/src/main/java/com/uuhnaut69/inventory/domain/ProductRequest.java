package com.uuhnaut69.inventory.domain;

import javax.validation.constraints.NotBlank;

public record ProductRequest(@NotBlank String name, int stocks) {
}
