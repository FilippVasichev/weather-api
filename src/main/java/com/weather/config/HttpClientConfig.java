package com.weather.config;

import com.weather.client.ApiNinjasWeatherApi;
import com.weather.client.OpenWeatherMapWeatherApi;
import com.weather.client.YandexWeatherApi;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/*
Configuration class responsible for creating HTTP clients for each weather APIs.
*/

@Configuration
public class HttpClientConfig {
    @Bean
    YandexWeatherApi yandexWeatherClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weather.yandex.ru/")
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return retrofit.create(YandexWeatherApi.class);
    }

    @Bean
    ApiNinjasWeatherApi apiNinjasWeatherClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.api-ninjas.com/")
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return retrofit.create(ApiNinjasWeatherApi.class);
    }

    @Bean
    OpenWeatherMapWeatherApi openweatherWeatherClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return retrofit.create(OpenWeatherMapWeatherApi.class);
    }
}
