package com.weather.controller;

import com.weather.AbstractComponentTest;
import com.weather.tables.pojos.Weather;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled
public class WeatherControllerTest extends AbstractComponentTest {
    /**
     * Makes real http call to weather APIs.
     */
    @Test
    @DisplayName("should successfully fetch all weather from APIs and save it to DB.")
    public void testFetchWeatherForAllCountries() {
        given()
                .port(port)
                .when()
                .get(basePath + "/fetch_weather")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(CoreMatchers.equalTo("Weather saved successfully."));
        List<Weather> allWeather = weatherStorage.getAllWeather();
        Weather weather = allWeather.getFirst();
        assertThat(allWeather, not(empty()));
        assertThat(weather.getCountry(), notNullValue());
        assertThat(weather.getCity(), notNullValue());
        assertThat(weather.getTemperature(), greaterThan(0));
    }

    @Test
    @DisplayName("should successfully get all weather from DB.")
    public void testGetAllWeatherFromDB() {
        createMoscowWeather();
        List<Weather> allWeather =
                given()
                        .port(port)
                        .when()
                        .get(basePath + "/get/all")
                        .then()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .body()
                        .jsonPath()
                        .getList(".", Weather.class);
        Weather moscowWeather = allWeather.getFirst();
        assertThat(allWeather, not(empty()));
        assertNotNull(moscowWeather);
        assertThat(moscowWeather.getCountry(), equalTo("Russia"));
        assertThat(moscowWeather.getCity(), equalTo("Moscow"));
        assertThat(moscowWeather.getTemperature(), equalTo(15));
        assertThat(moscowWeather.getDate(), equalTo(Timestamp.valueOf("2024-07-17 01:30:00").toLocalDateTime()));
    }

    @Test
    @DisplayName("should successfully get weather with date.")
    public void testGetWeatherByDate() {
        createBerlinWeather();
        Weather berlinWeather =
                given().
                        port(port)
                        .queryParam("country", "Germany")
                        .queryParam("city", "Berlin")
                        .queryParam("date", "1999-12-30T14:15:00")
                        .when()
                        .get(basePath + "/get")
                        .then()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(Weather.class);
        assertNotNull(berlinWeather);
        assertThat(berlinWeather.getId(), notNullValue());
        assertThat(berlinWeather.getCountry(), notNullValue());
        assertThat(berlinWeather.getCity(), notNullValue());
        assertThat(berlinWeather.getTemperature(), notNullValue());
        assertThat(berlinWeather.getDate(), notNullValue());
    }

    @Test
    @DisplayName("should successfully get weather without date.")
    public void testGetWeatherWithoutDate() {
        createParisWeather();
        Weather parisWeather =
                given().
                        port(port)
                        .queryParam("country", "France")
                        .queryParam("city", "Paris")
                        .when()
                        .get(basePath + "/get")
                        .then()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(Weather.class);
        assertNotNull(parisWeather);
        assertThat(parisWeather.getId(), notNullValue());
        assertThat(parisWeather.getCountry(), notNullValue());
        assertThat(parisWeather.getCity(), notNullValue());
        assertThat(parisWeather.getTemperature(), notNullValue());
        assertThat(parisWeather.getDate(), notNullValue());
    }
}
