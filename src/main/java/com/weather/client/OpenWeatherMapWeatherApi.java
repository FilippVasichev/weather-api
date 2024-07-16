package com.weather.client;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapWeatherApi {
    /*
    Retrofit2 HTTP client configuration for making GET call to open weather map.
     */
    @GET("data/2.5/weather?units=metric")
    Call<JsonNode> getOpenWeather(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("appid") String apiKey
    );
}
