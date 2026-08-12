package com.thelastimperial.resmail;

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
public class ResmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResmailApplication.class, args);
	}

}
