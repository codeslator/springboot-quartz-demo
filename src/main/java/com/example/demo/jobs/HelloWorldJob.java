package com.example.demo.jobs;

import com.example.demo.utils.TimerInfo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HelloWorldJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap(); // Getting details from job
        TimerInfo timerInfo = (TimerInfo) jobDataMap.get(HelloWorldJob.class.getSimpleName());
        log.info("Remaining fire count {}", timerInfo.getRemainingFireCount());
    }
}
