package com.weather.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties("locations")
public class LocationProperties {
    private final Map<String, List<City>> countries;

    public LocationProperties(Map<String, List<City>> countries) {
        this.countries = countries;
    }

    public Map<String, List<City>> getCountries() {
        return countries;
    }

    public record City(String city, double lat, double lon) { }
}
