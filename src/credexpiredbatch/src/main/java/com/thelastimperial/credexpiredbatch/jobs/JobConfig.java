package com.thelastimperial.credexpiredbatch.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfig {
    @Bean
    public Job credentialExpiredJob(JobRepository jobRepository, Step credentialExpiredStep){
        return new JobBuilder("CredentialExpiredJob", jobRepository)
            .start(credentialExpiredStep)
            .build();
    }
}
