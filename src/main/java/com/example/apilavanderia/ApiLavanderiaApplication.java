package com.example.apilavanderia;

import com.example.apilavanderia.database.Database;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiLavanderiaApplication {

	public static void main(String[] args) {
		system.out.println("add feature");
		Database.createUser();
		SpringApplication.run(ApiLavanderiaApplication.class, args);
	}

}
