package com.uuhnaut69.inventory.domain;

import jakarta.validation.constraints.NotBlank;

public record ProductRequest(@NotBlank String name, int stocks) {

}
