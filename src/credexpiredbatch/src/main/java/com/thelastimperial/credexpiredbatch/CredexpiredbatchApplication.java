package com.thelastimperial.credexpiredbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;

@EnableTask
@SpringBootApplication
public class CredexpiredbatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(CredexpiredbatchApplication.class, args);
	}

	@Bean
	public RunnerTask runnerTask(JobLauncher jobLauncher, Job credentialExpiredJob) {
		return new RunnerTask(jobLauncher, credentialExpiredJob);
	}

	public static class RunnerTask implements CommandLineRunner{
		private final JobLauncher launcher;
        private final Job credentialExpiredJob;


        public RunnerTask(JobLauncher launcher, Job job1) {
            this.launcher = launcher;
            this.credentialExpiredJob = job1;
        }
		@Override
		public void run(String... args) throws Exception {
			launcher.run(credentialExpiredJob, new JobParameters());
		}

	}

}
