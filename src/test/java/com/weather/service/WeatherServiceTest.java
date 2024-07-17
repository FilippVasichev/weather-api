package com.weather.service;


import com.weather.AbstractComponentTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

public class WeatherServiceTest extends AbstractComponentTest {

    @Test
    @DisplayName("should successfully parse temperature from yandex response")
    public void testGetYandexResponse() throws IOException {
        Map<String, Double> moscow = moscowCoordinates();
        int moscowWeather = weatherService.getYandexWeatherTemp(moscow.get("lat"), moscow.get("lon"));
        assertThat(moscowWeather, anyOf(greaterThan(0), lessThan(0)));
    }


    @Test
    @DisplayName("should successfully parse temperature from openWeather response")
    public void testGetOpenWeatherResponse() throws IOException {
        Map<String, Double> moscow = moscowCoordinates();
        int moscowWeather = weatherService.getOpenweatherWeatherTemp(moscow.get("lat"), moscow.get("lon"));
        assertThat(moscowWeather, anyOf(greaterThan(0), lessThan(0)));

    }

    @Test
    @DisplayName("should successfully parse temperature from apiNinjas response")
    public void testApiNinjasResponse() throws IOException {
        Map<String, Double> moscow = moscowCoordinates();
        int moscowWeather = weatherService.getApiNinjasWeatherTemp(moscow.get("lat"), moscow.get("lon"));
        assertThat(moscowWeather, anyOf(greaterThan(0), lessThan(0))
        );
    }
}
