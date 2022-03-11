package com.example.demo.services;

import com.example.demo.jobs.HelloWorldJob;
import com.example.demo.utils.TimerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaygroundService {

    private final SchedulerService schedulerService;

    @Autowired
    public PlaygroundService(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    public void runHelloWorldJob() {
        final TimerInfo timerInfo = new TimerInfo();
        timerInfo.setRunForever(false);
        timerInfo.setTotalFireCount(5);
        timerInfo.setRemainingFireCount(timerInfo.getTotalFireCount());
        timerInfo.setRepeatIntervalMs(2000L);
        timerInfo.setInitialOffsetMs(1000L);
        timerInfo.setCallbackData("my callbacik data");
        schedulerService.schedule(HelloWorldJob.class, timerInfo);
    }

    public List<TimerInfo> getAllRunningJobs() {
        return schedulerService.getAllTimerInfo();
    }

    public TimerInfo getRunningJobByTimerId(String timerId) {
        return schedulerService.getRunningTimer(timerId);
    }

    public Boolean deleteTimer(final String timerId) {
        return schedulerService.deleteTimer(timerId);
    }

    public void updateTimer(final String timerId, final TimerInfo timerInfo) {

    }
}
