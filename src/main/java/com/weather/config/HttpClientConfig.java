package com.weather.config;

import com.weather.client.YandexWeatherApi;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class HttpClientConfig {
    @Bean
    YandexWeatherApi yandexWeather() {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weather.yandex.ru/")
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return retrofit.create(YandexWeatherApi.class);
    }
}
