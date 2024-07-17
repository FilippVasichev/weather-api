package com.weather;

import com.weather.config.HttpClientConfig;
import com.weather.service.WeatherService;
import com.weather.service.config.WeatherApiProperties;
import com.weather.storage.WeatherStorage;
import org.jooq.DSLContext;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import static com.weather.tables.Weather.WEATHER;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = PostgreSQLContainerInitializer.class)
public class AbstractComponentTest {

    protected String basePath = "/api/v1/weather";

    @LocalServerPort
    protected int port = 0;

    @Autowired
    protected WeatherService weatherService;

    @Autowired
    protected WeatherStorage weatherStorage;

    @Autowired
    protected DSLContext dslContext;

    @Autowired
    protected HttpClientConfig httpClientConfig;

    @Autowired
    protected WeatherApiProperties weatherApiProperties;

    @AfterEach
    protected void cleanUpDb() {
        dslContext.truncate(WEATHER).cascade().execute();
    }

    protected void createMoscowWeather() {
        weatherStorage.insert(
                "Moscow",
                "Russia",
                15,
                Timestamp.valueOf("2024-07-17 01:30:00"));
    }

    protected void createBerlinWeather() {
        weatherStorage.insert(
                "Berlin",
                "Germany",
                20,
                Timestamp.valueOf("1999-12-30 14:15:00"));
    }

    protected void createParisWeather() {
        weatherStorage.insert(
                "Paris",
                "France",
                25,
                Timestamp.valueOf("2021-09-11 14:15:00"));
    }

    protected Map<String, Double> moscowCoordinates() {
        Map<String, Double> coordinates = new HashMap<>();
        coordinates.put("lat", 55.7558);
        coordinates.put("lon", 37.6176);
        return coordinates;
    }
}
