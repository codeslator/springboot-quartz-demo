package com.example.demo.controllers;

import com.example.demo.services.PlaygroundService;
import com.example.demo.utils.TimerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/timer")
public class PlaygroundController {

    private final PlaygroundService playgroundService;

    @Autowired
    public PlaygroundController(PlaygroundService playgroundService) {
        this.playgroundService = playgroundService;
    }

    @PostMapping("/runHelloWorld")
    public void runHelloWorld() {
        playgroundService.runHelloWorldJob();
    }

    @GetMapping
    public List<TimerInfo> getAllRunningJobs() {
        return playgroundService.getAllRunningJobs();
    }

    @GetMapping("/{timerId}")
    public TimerInfo getOneRunningJob(@PathVariable String timerId) {
        return playgroundService.getRunningJobByTimerId(timerId);
    }

    @DeleteMapping("/{timerId}")
    public Boolean deleteJob(@PathVariable String timerId) {
        return playgroundService.deleteTimer(timerId);
    }
}
