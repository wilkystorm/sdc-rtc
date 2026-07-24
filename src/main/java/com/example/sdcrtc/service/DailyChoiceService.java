package com.example.sdcrtc.service;

import com.example.sdcrtc.client.XaiApiClient;
import com.example.sdcrtc.model.DailyChoice;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DailyChoiceService {

    private final XaiApiClient xaiApiClient;

    public DailyChoiceService(XaiApiClient xaiApiClient) {
        this.xaiApiClient = xaiApiClient;
    }

    @Cacheable(value = "dailyChoice", key = "T(java.time.LocalDate).now().toString()")
    public DailyChoice getToday() {
        return xaiApiClient.getDailyChoice();
    }

    // For testing or manual refresh
    public DailyChoice refreshToday() {
        return xaiApiClient.getDailyChoice();
    }
}
