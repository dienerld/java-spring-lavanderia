package com.example.apilavanderia.controllers;

import com.example.apilavanderia.dtos.RequestLogin;
import com.example.apilavanderia.database.Database;
import com.example.apilavanderia.dtos.ResponseError;
import com.example.apilavanderia.repositories.ApartmentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private ApartmentRepository repository;

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid RequestLogin login) {
        try {
            var apt = repository.getReferenceById(login.number());
            if (!apt.getPassword().equals(login.password())) {
                return ResponseEntity.badRequest().body(new ResponseError("Credenciais inválidas.", "Unauthorized"));
            }
            var token = apt.generateToken();
            repository.save(apt);
            return ResponseEntity.ok().body(token);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(new ResponseError("Credenciais inválidas.", e.getClass().getName()));
        }


    }

}
