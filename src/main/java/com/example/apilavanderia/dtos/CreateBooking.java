package com.example.apilavanderia.dtos;

import com.example.apilavanderia.enums.Machine;
import com.example.apilavanderia.enums.Shift;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateBooking(
        @NotBlank String apartment, @NotNull Machine machine, @NotNull @FutureOrPresent LocalDate date,
        @NotNull Shift hour) {
}
