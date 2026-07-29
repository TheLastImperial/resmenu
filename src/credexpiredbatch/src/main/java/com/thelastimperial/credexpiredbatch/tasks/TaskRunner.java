package com.thelastimperial.credexpiredbatch.tasks;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskRunner implements CommandLineRunner{
    private final JobLauncher jobLauncher;
    private final Job credentialExpiredJob;

    public TaskRunner(JobLauncher jobLauncher, Job credentialExpiredJob){
        this.jobLauncher = jobLauncher;
        this.credentialExpiredJob = credentialExpiredJob;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting job: {}", credentialExpiredJob.getName());
        jobLauncher.run(credentialExpiredJob, new JobParameters());
    }
    
}
