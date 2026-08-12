package com.thelastimperial.resmenu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan( basePackages = {
	"com.thelastimperial.resdomain.entities"
})
@EnableJpaRepositories( basePackages = {
	"com.thelastimperial.resdomain.repositories"
})
public class ResmenuApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResmenuApplication.class, args);
	}

}
