package com.example.apilavanderia.controllers;

import com.example.apilavanderia.dtos.RequestLogin;
import com.example.apilavanderia.database.Database;
import com.example.apilavanderia.dtos.ResponseError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    Database database;

    public AuthController() {
        database = new Database();
    }

    @PostMapping
    public ResponseEntity login(@RequestBody RequestLogin login) {
        try {
            var apt = database.getApartmentByNumber(login.number());
            if(!apt.getPassword().equals(login.password())) {
                return ResponseEntity.badRequest().body(new ResponseError("Credenciais inválidas.", "Unauthorized"));
            }

            return ResponseEntity.ok().body(apt.generateToken());
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(new ResponseError("Credenciais inválidas.", e.getClass().getName()));
        }


    }

}
