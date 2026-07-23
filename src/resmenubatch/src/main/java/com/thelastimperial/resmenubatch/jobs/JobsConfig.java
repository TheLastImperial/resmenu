package com.thelastimperial.resmenubatch.jobs;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobsConfig {
    @Bean
    public Job accountExpiredJob(JobRepository jobRepository, Step expiredAccountStep){
        return new JobBuilder("AccountExpiredJob", jobRepository)
            .start(expiredAccountStep)
            .build();
    }
}
