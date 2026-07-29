package com.thelastimperial.resmenubatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.test.JobOperatorTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

@SpringBatchTest
@SpringBootTest
public class AccountExpiredJobTest {
    @Autowired
    private Job accountExpiredJob;

    @Autowired
    private JobOperatorTestUtils jobOperatorTestUtils;
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    public void accountExpiredJobTest() throws Exception {
        jobOperatorTestUtils.setJob(accountExpiredJob);
        JobExecution jobExe = jobOperatorTestUtils.startJob();
        assertEquals(ExitStatus.COMPLETED, jobExe.getExitStatus());
        assertEquals(1,
            JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate,
                "user_audits", "action=0"
            )
        );
        
        assertEquals(
            2,
            JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "users", "account_non_expired='f'"
            )
        );
    }
}
