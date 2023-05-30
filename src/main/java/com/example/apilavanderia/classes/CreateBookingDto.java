package com.example.apilavanderia.classes;

import com.example.apilavanderia.enums.Machine;
import com.example.apilavanderia.enums.Shift;

import java.time.LocalDate;

public record CreateBookingDto(String apartment, Machine machine, LocalDate date, Shift hour) {
}
