package com.DYShunyaev.TelRosSoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class TelRosSoftApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelRosSoftApplication.class, args);
	}

}
