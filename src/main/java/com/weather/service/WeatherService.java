package com.weather.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.weather.client.ApiNinjasWeatherApi;
import com.weather.client.OpenWeatherMapWeatherApi;
import com.weather.client.YandexWeatherApi;
import com.weather.service.config.LocationProperties;
import com.weather.service.config.WeatherApiProperties;
import com.weather.storage.WeatherStorage;
import com.weather.tables.pojos.Weather;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {
    private final YandexWeatherApi yandexWeatherApi;
    private final ApiNinjasWeatherApi apiNinjasWeatherApi;
    private final OpenWeatherMapWeatherApi openWeatherMapWeatherApi;
    private final WeatherStorage weatherStorage;
    private final WeatherApiProperties weatherApiProperties;
    private final LocationProperties locationProperties;
    private final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    public WeatherService(
            YandexWeatherApi yandexWeatherApi,
            ApiNinjasWeatherApi apiNinjasWeatherApi,
            OpenWeatherMapWeatherApi openWeatherMapWeatherApi,
            WeatherStorage weatherStorage,
            WeatherApiProperties weatherApiProperties,
            LocationProperties locationProperties
    ) {
        this.yandexWeatherApi = yandexWeatherApi;
        this.apiNinjasWeatherApi = apiNinjasWeatherApi;
        this.openWeatherMapWeatherApi = openWeatherMapWeatherApi;
        this.weatherStorage = weatherStorage;
        this.weatherApiProperties = weatherApiProperties;
        this.locationProperties = locationProperties;
    }

    public int getYandexWeatherTemp(double lat, double lon) throws IOException {
        Call<JsonNode> call = yandexWeatherApi.getYandexWeather(lat, lon, weatherApiProperties.getYandexKey());
        Response<JsonNode> response = call.execute();
        if (response.isSuccessful()) {
            JsonNode json = response.body();
            if (json != null) {
                return json.get("fact").get("temp").asInt();
            } else {
                logger.warn("Error while fetching weather data from {}", call.request().url());
                throw new IOException("Invalid JSON structure");
            }
        } else {
            throw new IOException("Request failed with error code: " + response.code());
        }
    }

    public int getApiNinjasWeatherTemp(double lat, double lon) throws IOException {
        Call<JsonNode> call = apiNinjasWeatherApi.getApiNinjasWeather(lat, lon, weatherApiProperties.getApiNinjasKey());
        Response<JsonNode> response = call.execute();
        if (response.isSuccessful()) {
            JsonNode json = response.body();
            if (json != null) {
                return json.get("temp").asInt();
            } else {
                logger.warn("Error while fetching weather data from {}", call.request().url());
                throw new IOException("Invalid JSON structure");
            }
        } else {
            throw new IOException("Request failed with error code: " + response.code());
        }
    }

    public int getOpenweatherWeatherTemp(double lat, double lon) throws IOException {
        Call<JsonNode> call = openWeatherMapWeatherApi.getOpenWeather(lat, lon, weatherApiProperties.getOpenWeatherMap());
        Response<JsonNode> response = call.execute();
        if (response.isSuccessful()) {
            JsonNode json = response.body();
            if (json != null) {
                return json.get("main").get("temp").asInt();
            } else {
                logger.warn("Error while fetching weather data from {}", call.request().url());
                throw new IOException("Invalid JSON structure");
            }
        } else {
            throw new IOException("Request failed with error code: " + response.code());
        }
    }

    private int callApiAndGetAverageTemperature(double lat, double lon) throws IOException {
        /* Calls api and calculate avg temp. */
        int countOfApis = 3;
        return (
                getOpenweatherWeatherTemp(lat, lon) +
                        getYandexWeatherTemp(lat, lon) +
                        getApiNinjasWeatherTemp(lat, lon)
        ) / countOfApis;
    }

    public ResponseEntity<String> getAverageWeatherForAllLocationsAndSaveToDB() {
        /*
        Get all cities from locationProperies
        and make api calls for all cities and saves it to db.
        */
        try {
            Map<String, List<LocationProperties.City>> countries = locationProperties.getCountries();
            for (Map.Entry<String, List<LocationProperties.City>> entry : countries.entrySet()) {
                String country = entry.getKey();
                List<LocationProperties.City> cities = entry.getValue();
                for (LocationProperties.City city : cities) {
                    weatherStorage.insert(
                            city.city(),
                            country,
                            callApiAndGetAverageTemperature(city.lat(), city.lon()),
                            Timestamp.from(Instant.now())
                    );
                }
            }
            return ResponseEntity.ok("Weather saved successfully.");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to fetch weather data: " + e.getMessage());
        }
    }

    public List<Weather> getAllWeather() {
        return weatherStorage.getAllWeather();
    }

    public Weather getCityWeatherByDate(String country, String city, Timestamp date) {
        return weatherStorage.getCityWeatherByDate(country, city, date);
    }

    public Weather getCityWeatherWithoutDate(String country, String city) {
        return weatherStorage.getCityWeatherWithoutDate(country, city);
    }
}
