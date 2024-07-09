package com.weather.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.weather.client.YandexWeatherApi;
import com.weather.model.Weather;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;

@Service
public class WeatherService {
    private final YandexWeatherApi yandexWeatherApi;

    public WeatherService(YandexWeatherApi yandexWeatherApi) {
        this.yandexWeatherApi = yandexWeatherApi;
    }

    public int getYandexWeatherTemp(double lat, double lon, String apiKey) throws IOException {
        Call<JsonNode> call = yandexWeatherApi.getYandexWeather(lat, lon, apiKey);
        Response<JsonNode> response = call.execute();
        if (response.isSuccessful()) {
            JsonNode json = response.body();
            if (json != null) {
                return json.get("fact").get("temp").asInt();
            } else {
                throw new IOException("Invalid JSON structure");
            }
        } else {
            throw new IOException("Request failed with error code: " + response.code());
        }
    }

    public Weather getAllWeather(double lat, double lon, String apiKey) throws IOException {
        return new Weather(
                "Moscow",
                getYandexWeatherTemp(lat, lon, apiKey),
                Timestamp.from(OffsetDateTime.now().toInstant())
        );
    }
}