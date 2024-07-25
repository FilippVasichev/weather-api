package com.weather.controller;


import com.weather.service.WeatherService;
import com.weather.tables.pojos.Weather;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {
    private static final Logger log = LoggerFactory.getLogger(WeatherController.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {this.weatherService = weatherService;}

    /**
     * Start fetch weather after "initial delay", with "fixed delay" rate.
     */
    @Scheduled(
            initialDelayString = "${weather-api-settings.initial-delay}",
            fixedDelayString = "${weather-api-settings.call-interval}"
    )
    public ResponseEntity<String> fetchWeatherBySchedule() {
        log.info("Automatic weather fetch, time: {}", dateFormat.format(new Date()));
        return weatherService.getAverageWeatherForAllLocationsAndSaveToDB();
    }

    @PostMapping("/manually_fetch_weather")
    public ResponseEntity<String> fetchAllWeather() {
        return weatherService.getAverageWeatherForAllLocationsAndSaveToDB();
    }

    @GetMapping()
    public ResponseEntity<Weather> getWeather(
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date
    ) {
        Weather weather;
        if (date != null) {
            Timestamp timestamp = Timestamp.valueOf(date);
            weather = weatherService.getCityWeatherByDate(country, city, timestamp);
        } else {
            weather = weatherService.getCityWeatherWithoutDate(country, city);
        }
        if (weather != null) {
            return ResponseEntity.ok(weather);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public List<Weather> getAllWeather() {
        return weatherService.getAllWeather();
    }
}

