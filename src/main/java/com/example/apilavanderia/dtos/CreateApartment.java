package com.example.apilavanderia.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateApartment(
        @NotNull
        @NotBlank
        String number) {

}
