package com.uuhnaut69.customer.api.request;

import javax.validation.constraints.NotBlank;

public record CustomerRequest(@NotBlank String username, @NotBlank String fullName) {
}
