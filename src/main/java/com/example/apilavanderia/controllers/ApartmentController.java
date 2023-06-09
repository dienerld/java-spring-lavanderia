package com.example.apilavanderia.controllers;

import com.example.apilavanderia.dtos.*;
import com.example.apilavanderia.models.Apartment;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.apilavanderia.repositories.ApartmentRepository;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/apartments")

public class ApartmentController {
    @Autowired
    private ApartmentRepository repository;


    @GetMapping
    public ResponseEntity getAll(
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Boolean hasLogged
    ) {
        var apartments = repository.findAll();

        if (phone != null) {
            apartments = apartments
                    .stream()
                    .filter(apt -> apt.getPhone() != null && apt.getPhone().contains((phone)))
                    .toList();
        }

        if (hasLogged != null) {
            apartments = apartments
                    .stream()
                    .filter(apt -> hasLogged ? apt.getTokenLogin() != null : apt.getTokenLogin() == null)
                    .toList();
        }


        return ResponseEntity.ok().body(apartments.stream().map(OutputApartment::new).toList());
    }

    @GetMapping("/{number}")
    public ResponseEntity getOne(@PathVariable String number, @RequestHeader("AuthToken") String token) {
        var apt = repository.getReferenceById(number);

        if (!apt.isAuthenticated(token)) {
            return ResponseEntity.badRequest()
                    .body(new ResponseError("Token Inválido", "Unauthorized"));
        }

        return ResponseEntity.ok().body(new OutputApartmentWithHistory(apt));

    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid CreateApartment newApt) {

        var aptExists = repository.existsById(newApt.number());
        if (aptExists) {
            return ResponseEntity.badRequest()
                    .body(new ResponseError("Apartamento já existente!", "."));
        }
        var apt = new Apartment(newApt);
        repository.save(apt);
        return ResponseEntity.ok().body(new OutputApartment(apt));
    }

    @PutMapping("/{number}")
    public ResponseEntity update(@RequestBody UpdateApartment dto,
                                 @PathVariable String number,
                                 @RequestHeader("AuthToken") String token) {

        try {

            var apt = repository.getReferenceById(number);

            if (!apt.isAuthenticated(token)) {
                return ResponseEntity.badRequest()
                        .body(new ResponseError("Token Inválido", "Unauthorized"));
            }

            if (dto.phone() != null) apt.setPhone(dto.phone());
            if (dto.nameResident() != null) apt.setNameResident(dto.nameResident());
            if (dto.password() != null) {
                if (dto.password().length() >= 6) {
                    apt.setPassword(dto.password());
                } else {
                    return ResponseEntity.badRequest().body(new ResponseError("A senha precisa ter no mínimo 6 caracteres.", "InvalidPassword."));
                }
            }
            repository.save(apt);

            return ResponseEntity.ok().body(apt);

        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage(), e.getClass().getCanonicalName()));
        }

    }

}
