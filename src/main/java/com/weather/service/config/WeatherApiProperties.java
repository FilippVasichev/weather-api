package com.weather.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for API keys.
 * <p>
 * Example YAML structure:
 * <pre>
 * weather-api-keys:
 *   yandex: key
 *   apiNinjas: key
 *   openweathermap: key
 * </pre>
 */


@ConfigurationProperties("weather-api-keys")
public class WeatherApiProperties {
    private final String yandex;
    private final String apiNinjas;
    private final String openWeatherMap;

    public WeatherApiProperties(
            String yandex,
            String apiNinjas,
            String openWeatherMap
    ) {
        this.yandex = yandex;
        this.apiNinjas = apiNinjas;
        this.openWeatherMap = openWeatherMap;
    }

    public String getYandexKey() {
        return yandex;
    }

    public String getApiNinjasKey() {
        return apiNinjas;
    }

    public String getOpenWeatherMap() {
        return openWeatherMap;
    }
}
