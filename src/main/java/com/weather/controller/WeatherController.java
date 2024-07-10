package com.weather.controller;


import com.weather.model.Weather;
import com.weather.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public final Weather getAllWeather(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam String apiKey
    ) throws IOException {
        return weatherService.getAllWeather(lat, lon, apiKey);
    }

//    @GetMapping
//    public Weather getWeather(
//            @RequestParam String city,
//            @RequestParam String country,
//            @RequestParam Timestamp date
//            ){
//        if (date != null){
//            return weatherStorage.getTemperatureNow(city, country);
//        }
//        return weatherStorage.getTemperatureByDate(city, country, date);
//    }
}
