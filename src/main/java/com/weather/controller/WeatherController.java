package com.weather.controller;


import com.weather.service.WeatherService;
import com.weather.tables.pojos.Weather;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {
    private final WeatherService weatherService;


    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/fetch_weather")
    public final ResponseEntity fetchAllWeather() throws IOException {
        return weatherService.getAverageWeatherForAllLocationsAndSaveToDB();
    }

    @GetMapping("/all")
    public final List<Weather> getAllWeather() throws IOException {
        return weatherService.getAllWeather();
    }

//    @GetMapping
//    public Weather getWeather(
//            @RequestParam String country,
//            @RequestParam String city,
//            @RequestParam Timestamp date
//            ){
//        if (date != null){
//            return weatherStorage.getTemperatureNow(city, country);
//        }
//        return weatherStorage.getTemperatureByDate(city, country, date);
//    }
}

