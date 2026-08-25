package com.thelastimperial.credexpiredbatch.tasks;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;

public class TaskConfig {

    public TaskRunner taskRunner(JobLauncher jobLauncher, Job credentialExpiredJob){
        return new TaskRunner(jobLauncher, credentialExpiredJob);
    }
}
