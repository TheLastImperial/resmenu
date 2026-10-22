package com.thelastimperial.resdomain.autoconfiguration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@EntityScan(
    basePackages = "com.thelastimperial.resdomain.entities"
)
@EnableJpaRepositories (
    basePackages = "com.thelastimperial.resdomain.repositories"
)
public class ResdomainAutoConfiguration {
}
