package com.example.apilavanderia.models;

import com.example.apilavanderia.database.Database;
import com.example.apilavanderia.dtos.CreateBooking;
import com.example.apilavanderia.enums.Machine;
import com.example.apilavanderia.enums.Shift;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "bookings")
@AllArgsConstructor
@NoArgsConstructor

public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(referencedColumnName = "number", name = "apartment_FK")
    private Apartment apartment;

    private LocalDate date;

    private Shift hour;

    private Machine machine;

    public Booking(CreateBooking newBooking, Apartment apt) {
        apartment = apt;
        date = newBooking.date();
        hour = newBooking.hour();
        machine = newBooking.machine();
    }
}

