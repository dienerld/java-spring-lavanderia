package com.example.apilavanderia.classes;

import com.example.apilavanderia.database.Database;
import com.example.apilavanderia.enums.Machine;
import com.example.apilavanderia.enums.Shift;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.TemporalField;

@Getter
@Setter
public class Booking {

    private int id;

    private Apartment apartment;

    private LocalDate date;

    private Shift hour;

    private Machine machine;

    public Booking(CreateBookingDto newBooking, Apartment apt) {
        id = Database.bookings.size()+1;
        apartment = apt;
        date = newBooking.date();
        hour = newBooking.hour();
        machine = newBooking.machine();
    }
}

