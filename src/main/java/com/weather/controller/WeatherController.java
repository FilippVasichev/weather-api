package com.weather.controller;


import com.weather.service.WeatherService;
import com.weather.tables.pojos.Weather;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/fetch_weather")
    public ResponseEntity<String> fetchAllWeather() {
        return weatherService.getAverageWeatherForAllLocationsAndSaveToDB();
    }

    @GetMapping("/get")
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

    @GetMapping("/get/all")
    public List<Weather> getAllWeather() {
        return weatherService.getAllWeather();
    }
}

