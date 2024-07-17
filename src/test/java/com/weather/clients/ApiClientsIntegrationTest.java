package com.weather.clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.weather.AbstractComponentTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import retrofit2.Response;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for verifying the settings in application.yaml for API keys.
 * <p>
 * This class performs checks by invoking real API calls using the keys specified in the configuration.
 */

public class ApiClientsIntegrationTest extends AbstractComponentTest {

    @Test
    @DisplayName("should successfully get weather from yandexWeather.")
    public void testGetYandexResponse() throws IOException {
        Map<String, Double> moscow = moscowCoordinates();
        Response<JsonNode> yandexResponse = httpClientConfig.yandexWeatherClient().getYandexWeather(
                moscow.get("lat"),
                moscow.get("lon"),
                weatherApiProperties.getYandexKey()
        ).execute();
        assertEquals(yandexResponse.code(), HttpStatus.OK.value());
        assertThat(yandexResponse.body(), notNullValue());
    }

    @Test
    @DisplayName("should successfully get weather from apiNinjas.")
    public void testGetApiNinjasResponse() throws IOException {
        Map<String, Double> moscow = moscowCoordinates();
        Response<JsonNode> apiNinjasResponse = httpClientConfig.apiNinjasWeatherClient().getApiNinjasWeather(
                moscow.get("lat"),
                moscow.get("lon"),
                weatherApiProperties.getApiNinjasKey()
        ).execute();
        assertEquals(apiNinjasResponse.code(), HttpStatus.OK.value());
        assertThat(apiNinjasResponse.body(), notNullValue());
    }

    @Test
    @DisplayName("should successfully get weather from openWeather.")
    public void testGetOpenWeatherResponse() throws IOException {
        Map<String, Double> moscow = moscowCoordinates();
        Response<JsonNode> openWeather = httpClientConfig.openweatherWeatherClient().getOpenWeather(
                moscow.get("lat"),
                moscow.get("lon"),
                weatherApiProperties.getOpenWeatherMap()
        ).execute();
        assertEquals(openWeather.code(), HttpStatus.OK.value());
        assertThat(openWeather.body(), notNullValue());
    }
}
