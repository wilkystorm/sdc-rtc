package com.example.sdcrtc.model;

import java.time.LocalDate;

public record DailyChoice(
        LocalDate date,
        String choice
) {
}
