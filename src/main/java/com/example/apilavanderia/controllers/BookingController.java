package com.example.apilavanderia.controllers;

import com.example.apilavanderia.customExceptions.BookingException;
import com.example.apilavanderia.customExceptions.UnauthorizedException;
import com.example.apilavanderia.dtos.ResponseError;
import com.example.apilavanderia.enums.Machine;
import com.example.apilavanderia.enums.Shift;
import com.example.apilavanderia.models.Booking;
import com.example.apilavanderia.dtos.CreateBooking;
import com.example.apilavanderia.dtos.OutputBooking;
import com.example.apilavanderia.repositories.ApartmentRepository;
import com.example.apilavanderia.repositories.BookingRepository;
import com.example.apilavanderia.repositories.specifications.BookingsSpecifications;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @GetMapping
    public ResponseEntity<List<OutputBooking>> getAll(
            @RequestParam(required = false) @Valid Machine machine,
            @RequestParam(required = false) @Valid Shift hour
    ) {
        var bookingsFiltered = bookingRepository.findAll(BookingsSpecifications.filters(machine, hour));
        return ResponseEntity.ok().body(bookingsFiltered.stream().map(OutputBooking::new).toList());
    }

    @PostMapping
    public ResponseEntity<OutputBooking> create(@RequestBody @Valid CreateBooking newBooking) {

        var apt = apartmentRepository.getReferenceById(newBooking.apartment());
//        if (apt.isAuthenticated(token)) {
//            throw new UnauthorizedException("Token invalido");
//        }
        var bookings = bookingRepository.findByApartmentAndDateBetween(
                apt,
                newBooking.date().minusDays(4),
                newBooking.date().plusDays(4)
        );


        if (!bookings.isEmpty())
            throw new BookingException("User com agendamento no periodo de +-4 dias!");


        // Verificar se tem agendamento para mesma data
        var machineHasOccupied = bookingRepository.findByDateAndMachineAndHour(
                newBooking.date(),
                newBooking.machine(),
                newBooking.hour()
        );

        if (!machineHasOccupied.isEmpty()) throw new BookingException("Maquina ja agendada neste horario.");
        var booking = new Booking(newBooking, apt);
        bookingRepository.save(booking);
        apt.addBooking(booking);

        return ResponseEntity.ok().body(new OutputBooking(booking));
    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id, @RequestHeader("AuthToken") String token) {

        var booking = bookingRepository.getReferenceById(id);

        if (booking.getApartment().isAuthenticated(token)) {
            throw new UnauthorizedException("Token invalido");
        }

        var today = LocalDate.now();
        if (today.isAfter(booking.getDate())) {
            throw new BookingException("Não é possível cancelar agendamentos passados.");
        }

        bookingRepository.delete(booking);

        return ResponseEntity.noContent().build();

    }

}
