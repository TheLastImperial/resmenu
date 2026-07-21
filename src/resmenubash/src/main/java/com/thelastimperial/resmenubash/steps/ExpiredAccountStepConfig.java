package com.thelastimperial.resmenubash.steps;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaCursorItemReader;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.infrastructure.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;

import com.thelastimperial.resmenubash.entities.UserEntity;
import com.thelastimperial.resmenubash.entities.UserSettingEntity;

import jakarta.persistence.EntityManagerFactory;

@Configuration
public class ExpiredAccountStepConfig {
    @Bean
    public Step expiredAccountStep(
        JobRepository jobRepository, EntityManagerFactory entityManagerFactory,
        ItemReader<UserSettingEntity> userSettingExpiredReader,
        ItemProcessor<UserSettingEntity, UserEntity> processor,
        ItemWriter<UserEntity> userExpiredWriter
    ){
        return new StepBuilder("ExpiredAccountStep",jobRepository)
            .<UserSettingEntity, UserEntity>chunk(10)
            .reader(userSettingExpiredReader)
            .processor(processor)
            .writer(userExpiredWriter)
            .transactionManager(
                new JpaTransactionManager(entityManagerFactory)
            )
            .build();
    }

    @Bean
    public JpaCursorItemReader<UserSettingEntity> userSettingExpiredReader(
        EntityManagerFactory entityManagerFactory
    ) {
        String queryString = """ 
            SELECT us FROM UserSettingEntity us WHERE us.accountExpiredAt < :today
        """;

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("today", LocalDateTime.now());

        return new JpaCursorItemReaderBuilder<UserSettingEntity>()
            .name("UserSettingReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString(queryString)
            .parameterValues(parameters)
            .build();
    }
    @Bean
    public JpaItemWriter<UserEntity> userExpiredWriter(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<UserEntity>()
            .entityManagerFactory(entityManagerFactory)
            .build();
    }
}
