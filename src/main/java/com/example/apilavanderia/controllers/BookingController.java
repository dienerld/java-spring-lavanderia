package com.example.apilavanderia.controllers;

import com.example.apilavanderia.customExceptions.BookingException;
import com.example.apilavanderia.dtos.ResponseError;
import com.example.apilavanderia.enums.Machine;
import com.example.apilavanderia.enums.Shift;
import com.example.apilavanderia.models.Booking;
import com.example.apilavanderia.dtos.CreateBooking;
import com.example.apilavanderia.dtos.OutputBooking;
import com.example.apilavanderia.database.Database;
import com.example.apilavanderia.repositories.ApartmentRepository;
import com.example.apilavanderia.repositories.BookingRepository;
import com.example.apilavanderia.repositories.specifications.BookingsSpecifications;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public ResponseEntity getAll(
            @RequestParam(required = false) @Valid Machine machine,
            @RequestParam(required = false) @Valid Shift hour
    ) {
        var bookingsFiltered = bookingRepository.findAll(BookingsSpecifications.filters(machine, hour));
        return ResponseEntity.ok().body(bookingsFiltered.stream().map(OutputBooking::new).toList());
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid CreateBooking newBooking, @RequestHeader("AuthToken") String token) {
        try {
            var apt = apartmentRepository.getReferenceById(newBooking.apartment());
            if (!apt.isAuthenticated(token)) {
                return ResponseEntity.badRequest()
                        .body(new ResponseError("Token Inválido", "Unauthorized"));
            }
            var bookings = bookingRepository.getBookingsByApartment(apt);

            // Verificar se usuário já possui agendamento no range de +-4 dias

            var filterApt = bookings.stream()
                    .filter(b -> b.getApartment().equals(apt))
                    .filter(b -> newBooking.date().minusDays(4).isAfter(b.getDate()))
                    .filter(b -> newBooking.date().plusDays(4).isBefore(b.getDate()))
                    .toList();

            if (filterApt.size() > 0)

                throw new BookingException("Usuário com agendamento no período de +-4 dias!");


            // Verificar se tem agendamento para mesma data
            var filteredList = bookings.stream()
                    .filter(b -> b.getDate().equals(newBooking.date()))
                    .filter(b -> b.getMachine().equals(newBooking.machine()))
                    .toList();

            if (filteredList.size() > 0) {
                // verifica se maquina esta ocupada no dia
                for (Booking b : filteredList) {
                    // Verificar se máquina está reservada para X hora
                    if (b.getHour().equals(newBooking.hour())) {
                        throw new BookingException("Máquina já agendada neste horário.");
                    }
                }
            }

            var booking = new Booking(newBooking, apt);
            bookingRepository.save(booking);
            apt.addBooking(booking);

            return ResponseEntity.ok().body(new OutputBooking(booking));

        } catch (BookingException e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage(), e.getClass().getCanonicalName()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseError(e.getMessage(), e.getClass().getCanonicalName()));

        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") UUID id, @RequestHeader("AuthToken") String token) {
        try {
            var booking = bookingRepository.getReferenceById(id);

            if (!booking.getApartment().isAuthenticated(token)) {
                return ResponseEntity.badRequest().body(new ResponseError("Token Inválido", "Unauthorized"));
            }

            var today = LocalDate.now();

            if (today.isAfter(booking.getDate())) {
                return ResponseEntity.badRequest().body(new ResponseError("Não é possivel realizar essa exclusão", "Bad Request Error"));
            }

            bookingRepository.delete(booking);

            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage(), e.getClass().getName()));
        }
    }

}
