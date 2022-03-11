package com.example.demo.services;

import com.example.demo.listeners.SimpleTriggerListener;
import com.example.demo.utils.TimerInfo;
import com.example.demo.utils.TimerUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SchedulerService {

    private final Scheduler scheduler;

    @Autowired
    public SchedulerService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void schedule(final Class jobClass, final TimerInfo timerInfo) { // Se encarga de ejecutar el job
        final JobDetail jobDetail = TimerUtils.buildJobDetail(jobClass, timerInfo);
        final Trigger trigger = TimerUtils.buildTrigger(jobClass, timerInfo);
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
    }

    @PostConstruct
    public void init() { // Inicia el Job
        try {
            scheduler.start();
            scheduler.getListenerManager().addTriggerListener(new SimpleTriggerListener(this));
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
    }

    @PreDestroy
    public void destroy() { // Finaliza el Job
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
    }

    public List<TimerInfo> getAllTimerInfo() { // Obtiene una lista de jobs
        try {
            return scheduler.getJobKeys(GroupMatcher.anyGroup()) // Obtiene todos los jobs de cualquier grupo a traves de cualquier key
                    .stream()
                    .map(jobKey -> {
                        try {
                            final JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                            return (TimerInfo) jobDetail.getJobDataMap().get(jobKey.getName());
                        } catch (SchedulerException e) {
                            log.error(e.getMessage());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            return Collections.emptyList(); // Si hubo un error, retorna una lista vacia
        }
    }

    public TimerInfo getRunningTimer(String timerId) {
        try {
            final JobDetail jobDetail = scheduler.getJobDetail(new JobKey(timerId));
            if(jobDetail == null) {
                return null;
            }
            return (TimerInfo) jobDetail.getJobDataMap().get(timerId);
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public void updateTimerInfo(final String timerId, final TimerInfo timerInfo) {
        try {
            final JobDetail jobDetail = scheduler.getJobDetail(new JobKey(timerId));
            if(jobDetail == null) {
                log.error("Failed to find timer with id {}", timerId);
                return;
            }
            jobDetail.getJobDataMap().put(timerId, timerInfo);
            scheduler.addJob(jobDetail, true, true);
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
    }

    public Boolean deleteTimer(final String timerId) {
        try {
            return scheduler.deleteJob(new JobKey(timerId));
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
