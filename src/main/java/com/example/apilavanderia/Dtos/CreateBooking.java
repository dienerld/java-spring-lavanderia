package com.example.apilavanderia.dtos;

import com.example.apilavanderia.enums.Machine;
import com.example.apilavanderia.enums.Shift;

import java.time.LocalDate;

public record CreateBooking(String apartment, Machine machine, LocalDate date, Shift hour) {
}
