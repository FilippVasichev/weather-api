package com.weather.client;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.*;

public interface YandexWeatherApi {
    @GET("v2/informers")
    Call<JsonNode> getYandexWeather(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Header("X-Yandex-Weather-Key") String apiKey
    );
}
