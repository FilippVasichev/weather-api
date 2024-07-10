package com.weather.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.weather.client.ApiNinjasWeatherApi;
import com.weather.client.OpenWeatherMapWeatherApi;
import com.weather.client.YandexWeatherApi;
import com.weather.model.Weather;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

@Service
public class WeatherService {
    private final YandexWeatherApi yandexWeatherApi;
    private final ApiNinjasWeatherApi apiNinjasWeatherApi;
    private final OpenWeatherMapWeatherApi openWeatherMapWeatherApi;

    public WeatherService(
            YandexWeatherApi yandexWeatherApi,
            ApiNinjasWeatherApi apiNinjasWeatherApi,
            OpenWeatherMapWeatherApi openWeatherMapWeatherApi
    ) {
        this.yandexWeatherApi = yandexWeatherApi;
        this.apiNinjasWeatherApi = apiNinjasWeatherApi;
        this.openWeatherMapWeatherApi = openWeatherMapWeatherApi;
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

    public int getApiNinjasWeatherTemp(double lat, double lon, String apiKey) throws IOException {
        Call<JsonNode> call = apiNinjasWeatherApi.getApiNinjasWeather(lat, lon, apiKey);
        Response<JsonNode> response = call.execute();
        if (response.isSuccessful()) {
            JsonNode json = response.body();
            if (json != null) {
                return json.get("temp").asInt();
            } else {
                throw new IOException("Invalid JSON structure");
            }
        } else {
            throw new IOException("Request failed with error code: " + response.code());
        }
    }

    public int getOpenweatherWeatherTemp(double lat, double lon, String apiKey) throws IOException {
        Call<JsonNode> call = openWeatherMapWeatherApi.getOpenWeather(lat, lon, apiKey);
        Response<JsonNode> response = call.execute();
        if (response.isSuccessful()) {
            JsonNode json = response.body();
            if (json != null) {
                return json.get("main").get("temp").asInt();
            } else {
                throw new IOException("Invalid JSON structure");
            }
        } else {
            throw new IOException("Request failed with error code: " + response.code());
        }
    }

    public Weather getAllWeather(double lat, double lon, String apiKey) throws IOException {
        return null;
    }
}
