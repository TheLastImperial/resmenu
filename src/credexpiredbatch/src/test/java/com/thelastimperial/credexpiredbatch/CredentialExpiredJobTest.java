package com.thelastimperial.credexpiredbatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

@SpringBatchTest
@SpringBootTest
public class CredentialExpiredJobTest {
    @Autowired
    Job accountExpiredJob;
    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    JobRepositoryTestUtils jobRepositoryTestUtils;

    @BeforeEach
    public void setUp() {
        jobRepositoryTestUtils.removeJobExecutions();
    }
    
    public void runTest() throws Exception {
        jobLauncherTestUtils.setJob(accountExpiredJob);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        assertEquals(1,
            JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate,
                "user_audits", "action=2"
            )
        );
        assertEquals(
            2,
            JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "users", "credentials_non_expired='f'"
            )
        );
    }
}
