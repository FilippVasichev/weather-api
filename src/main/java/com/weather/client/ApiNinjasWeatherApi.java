package com.weather.client;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiNinjasWeatherApi {
    @GET("v1/weather")
    Call<JsonNode> getApiNinjasWeather(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Header("X-Api-Key") String apiKey
    );
}
