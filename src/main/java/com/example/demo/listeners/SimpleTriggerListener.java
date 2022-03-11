package com.example.demo.listeners;

import com.example.demo.jobs.HelloWorldJob;
import com.example.demo.services.SchedulerService;
import com.example.demo.utils.TimerInfo;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.springframework.beans.factory.annotation.Autowired;

public class SimpleTriggerListener implements TriggerListener {
    private final SchedulerService schedulerService;

    @Autowired
    public SimpleTriggerListener(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }


    @Override
    public String getName() {
        return SimpleTriggerListener.class.getSimpleName();
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext jobExecutionContext) {
        final String timerId = trigger.getKey().getName();
        final JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap(); // Getting details from job
        final TimerInfo timerInfo = (TimerInfo) jobDataMap.get(timerId);

        if(!timerInfo.getRunForever()) {
            int remainingFireCount = timerInfo.getRemainingFireCount();
            if (remainingFireCount == 0) {
                return;
            }
            timerInfo.setRemainingFireCount(remainingFireCount - 1);
        }

        schedulerService.updateTimerInfo(timerId, timerInfo);
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext jobExecutionContext) {
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {

    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext jobExecutionContext, Trigger.CompletedExecutionInstruction completedExecutionInstruction) {

    }
}
