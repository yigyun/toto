package com.main.toto.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job updateBoardStatusJob;

    @Scheduled(cron = "0 0 0 * * ?")
    public void runUpdateBoardStatusJob() throws Exception {
        jobLauncher.run(updateBoardStatusJob, new JobParameters());}

}
