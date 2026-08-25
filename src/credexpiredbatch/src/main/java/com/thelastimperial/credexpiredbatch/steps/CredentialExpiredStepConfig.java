package com.thelastimperial.credexpiredbatch.steps;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.JdbcTransactionManager;

import com.thelastimperial.credexpiredbatch.domain.User;
import com.thelastimperial.credexpiredbatch.domain.enums.UserAuditAction;

@Configuration
public class CredentialExpiredStepConfig {

    @Bean
    public Step credentialExpiredStep(
        JobRepository jobRepository, JdbcTransactionManager jdbcTransactionManager,
        ItemReader<User> userCredentialExpiredReader,
        ItemWriter<User> userCredentialExpiredCompositeWriter
    ){
        return new StepBuilder("credentialExpiredStep", jobRepository)
            .<User, User>chunk(10, jdbcTransactionManager)
            .reader(userCredentialExpiredReader)
            .writer(userCredentialExpiredCompositeWriter)
            .build();
    }

    @Bean
    public ItemReader<User> userCredentialExpiredReader(DataSource dataSource) {
        String sql = """
            SELECT u.id
            FROM users u
            INNER JOIN user_settings us
                ON u.id = us.user_id
            WHERE us.credentials_expired_at < NOW()
                AND u.credentials_non_expired = 't'
        """;
        return new JdbcCursorItemReaderBuilder<User>()
            .name("UserCredentialExpiredReader")
            .dataSource(dataSource)
            .sql(sql)
            .rowMapper(new DataClassRowMapper<>(User.class))
            .build();
    }

    @Bean
    public ItemWriter<User> userCredentialExpiredCompositeWriter(
        ItemWriter<User> userCredentialExpiredWriter,
        ItemWriter<User> userAuditCredentialExpiredWriter
    ){
        CompositeItemWriter<User> compositeItemWriter = new CompositeItemWriter<>();
        List<ItemWriter<? super User>> delegates = new ArrayList<>();
        delegates.add(userCredentialExpiredWriter);
        delegates.add(userAuditCredentialExpiredWriter);
        compositeItemWriter.setDelegates(delegates);
        return compositeItemWriter;
    }

    @Bean
    public ItemWriter<User> userCredentialExpiredWriter(
        DataSource dataSource
    ){
        String sql = """
            UPDATE users
            SET credentials_non_expired = 'f'
            WHERE id = :id
        """;
        return new JdbcBatchItemWriterBuilder<User>()
            .dataSource(dataSource)
            .sql(sql)
            .itemSqlParameterSourceProvider(item -> {
                MapSqlParameterSource source = new MapSqlParameterSource();
                source.addValue("id", item.id());
                return source;
            })
            .build();
    }

    @Bean
    public ItemWriter<User> userAuditCredentialExpiredWriter(
        DataSource dataSource,
        @Value("${com.thelastimperial.credexpiredbatch.userid}") String updatedBy
    ){
        String sql = """
            INSERT INTO user_audits(id, user_id, action, updated_by, created_at, updated_at)
            VALUES(gen_random_uuid(), :id, :action, :updatedBy, NOW(), NOW())        
        """;
        return new JdbcBatchItemWriterBuilder<User>()
            .dataSource(dataSource)
            .sql(sql)
            .itemSqlParameterSourceProvider(item -> {
                MapSqlParameterSource source = new MapSqlParameterSource();
                source.addValue("id", item.id());
                source.addValue("action", UserAuditAction.CREDENTIALS_EXPIRED.ordinal());
                source.addValue("updatedBy", UUID.fromString(updatedBy));
                return source;
            })
            .build();
    }
}

