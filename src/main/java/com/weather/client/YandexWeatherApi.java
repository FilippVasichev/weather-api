package com.weather.client;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface YandexWeatherApi {
    /*
    Retrofit2 HTTP client configuration for making GET call to yandex weather.
     */
    @GET("v2/forecast")
    Call<JsonNode> getYandexWeather(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Header("X-Yandex-Weather-Key") String apiKey
    );
}
