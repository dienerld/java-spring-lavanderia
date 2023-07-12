package com.example.apilavanderia.models;
import com.example.apilavanderia.dtos.CreateApartment;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="apartments")
@AllArgsConstructor

public class Apartment {

    private String nameResident;

    @Id
    private String number;

    private String phone;

    private String password;

    private String tokenLogin;
    @OneToMany(mappedBy = "apartment")
    private List<Booking> bookings;

    public Apartment(String number){

        this.number = password = nameResident = number;
        bookings = new ArrayList<>();
    }
    public Apartment(CreateApartment newApt) {

        number = password = nameResident = newApt.number();
        bookings = new ArrayList<>();
    }

    public String generateToken(){
        tokenLogin = UUID.randomUUID().toString();
        return tokenLogin;
    }

    public boolean isAuthenticated(String token){
        return tokenLogin != null && tokenLogin.equals(token);
    }

    public void addBooking(Booking b) {
        bookings.add(b);
    }
}
