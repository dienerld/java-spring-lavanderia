package com.example.apilavanderia.controllers;

import com.example.apilavanderia.dtos.CreateApartment;
import com.example.apilavanderia.dtos.OutputApartment;
import com.example.apilavanderia.dtos.ResponseError;
import com.example.apilavanderia.dtos.UpdateApartment;
import com.example.apilavanderia.models.Apartment;
import com.example.apilavanderia.database.Database;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/apartments")
public class ApartmentController {
    Database database;

    public ApartmentController() {
        database = new Database();
    }

    @GetMapping
    public ResponseEntity getAll() {
        return ResponseEntity.ok().body(database.getApartments());
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid CreateApartment newApt) {
        var aptExists = database.getApartments().stream()
                .filter(apt -> apt.getNumber().equalsIgnoreCase(newApt.number()))
                .findFirst();
        if (aptExists.isPresent()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseError("Apartamento já existente!", "."));
        }
        var apt = new Apartment(newApt);
        database.addApartment(apt);
        return ResponseEntity.ok().body(new OutputApartment(apt));
    }

    @PutMapping("/{number}")
    public ResponseEntity update(@RequestBody  UpdateApartment dto,
                                 @PathVariable String number,
                                 @RequestHeader("AuthToken") String token) {

        try {

            var apt = database.getApartmentByNumber(number);

            if(!apt.isAuthenticated(token)){
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

            return ResponseEntity.ok().body(apt);

        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage(), e.getClass().getCanonicalName()));
        }

    }

}
