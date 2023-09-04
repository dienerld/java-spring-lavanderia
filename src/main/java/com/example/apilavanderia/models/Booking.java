package com.example.apilavanderia.models;

import com.example.apilavanderia.database.Database;
import com.example.apilavanderia.dtos.CreateBooking;
import com.example.apilavanderia.enums.Machine;
import com.example.apilavanderia.enums.Shift;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Booking {

    private int id;

    private Apartment apartment;

    private LocalDate date;

    private Shift hour;

    private Machine machine;

    public Booking(CreateBooking newBooking, Apartment apt) {
        id = Database.bookings.size()+1;
        apartment = apt;
        date = newBooking.date();
        hour = newBooking.hour();
        machine = newBooking.machine();
    }
}

