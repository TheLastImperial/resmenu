package com.thelastimperial.resmenubash.steps;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaCursorItemReader;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.infrastructure.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.infrastructure.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.infrastructure.item.support.CompositeItemProcessor;
import org.springframework.batch.infrastructure.item.support.CompositeItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.orm.jpa.JpaTransactionManager;

import com.thelastimperial.resmenubash.entities.UserEntity;
import com.thelastimperial.resmenubash.entities.UserSettingEntity;
import com.thelastimperial.resmenubash.entities.enums.UserAuditAction;

import jakarta.persistence.EntityManagerFactory;

@Configuration
public class ExpiredAccountStepConfig {
    @Bean
    public Step expiredAccountStep(
        JobRepository jobRepository, EntityManagerFactory entityManagerFactory,
        ItemReader<UserSettingEntity> userSettingExpiredReader,
        ItemProcessor<UserSettingEntity, UserEntity> userExpiredCompositeProcessor,
        ItemWriter<UserEntity> userExpiredCompositeWriter
    ){
        return new StepBuilder("ExpiredAccountStep",jobRepository)
            .<UserSettingEntity, UserEntity>chunk(10)
            .reader(userSettingExpiredReader)
            .processor(userExpiredCompositeProcessor)
            .writer(userExpiredCompositeWriter)
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

    @Bean
    public JdbcBatchItemWriter<UserEntity> userAuditExpiredWriter(DataSource dataSource){
        return new JdbcBatchItemWriterBuilder<UserEntity>()
            .dataSource(dataSource)
            .sql(
                """
                INSERT INTO user_audits(id, user_id, action, created_at, updated_at)
                VALUES(gen_random_uuid(), :id, :action, NOW(), NOW())
                """
            )
            .itemSqlParameterSourceProvider((item)-> {
                MapSqlParameterSource source = new MapSqlParameterSource();
                source.addValue("id", item.getId());
                source.addValue("action", UserAuditAction.ACCOUNT_EXPIRED.ordinal());
                return source;
            })
            .build();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public CompositeItemWriter<UserEntity> userExpiredCompositeWriter(
        JpaItemWriter<UserEntity> userExpiredWriter,
        JdbcBatchItemWriter<UserEntity> userAuditExpiredWriter
    ){
        CompositeItemWriter<UserEntity> compositeItemWriter = new CompositeItemWriter();
        List delegates = new ArrayList();
        delegates.add(userExpiredWriter);
        delegates.add(userAuditExpiredWriter);
        compositeItemWriter.setDelegates(delegates);
        return compositeItemWriter;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public CompositeItemProcessor<UserSettingEntity, UserEntity> userExpiredCompositeProcessor(
        ItemProcessor<UserSettingEntity, UserEntity> userSettingToUserProcessor,
        ItemProcessor<UserEntity, UserEntity> userExpiredAuditProcessor
    ){
        CompositeItemProcessor<UserSettingEntity, UserEntity> compositeItemProcessor =
            new CompositeItemProcessor();
        List delegates = new ArrayList();
        delegates.add(userSettingToUserProcessor);
        delegates.add(userExpiredAuditProcessor);
        compositeItemProcessor.setDelegates(delegates);
        return compositeItemProcessor;
    }
}
