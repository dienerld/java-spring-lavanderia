package com.example.apilavanderia.dtos;

import jakarta.validation.constraints.NotBlank;

public record RequestLogin(@NotBlank String number, @NotBlank String password) {
}
