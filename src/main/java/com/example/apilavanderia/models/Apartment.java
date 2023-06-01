package com.example.apilavanderia.models;
import com.example.apilavanderia.Dtos.CreateApartment;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter

public class Apartment {

    private String nameResident;

    private String number;

    private String phone;

    private String password;

    private String tokenLogin;

    public Apartment(CreateApartment newApt) {
        number = password = nameResident = newApt.number();
    }

    public String generateToken(){
        tokenLogin = UUID.randomUUID().toString();
        return tokenLogin;
    }

    public boolean isAuthenticated(String token){
        return tokenLogin != null && tokenLogin.equals(token);
    }
}
