package com.example.demo.utils;

import lombok.Data;

import java.io.Serializable;

@Data
public class TimerInfo implements Serializable {
    private Integer totalFireCount;
    private Integer remainingFireCount;
    private Boolean runForever;
    private Long repeatIntervalMs;
    private Long initialOffsetMs;
    private String callbackData;
}
