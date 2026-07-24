package com.example.sdcrtc.controller;

import com.example.sdcrtc.model.DailyChoice;
import com.example.sdcrtc.service.DailyChoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.format.DateTimeFormatter;

@Controller
public class DailyChoiceController {

    private final DailyChoiceService dailyChoiceService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM d, yyyy");

    public DailyChoiceController(DailyChoiceService dailyChoiceService) {
        this.dailyChoiceService = dailyChoiceService;
    }

    @GetMapping("/")
    public String home(Model model) {
        DailyChoice choice = dailyChoiceService.getToday();
        model.addAttribute("date", choice.date().format(DATE_FORMATTER));
        model.addAttribute("choice", choice.choice());
        return "index";
    }

    @GetMapping("/api/today")
    @ResponseBody
    public DailyChoice apiToday() {
        return dailyChoiceService.getToday();
    }
}
