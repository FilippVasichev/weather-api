package com.weather.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * Configuration properties for mapping countries and cities from application.yaml.
 * <p>
 * Example YAML structure:
 * <pre>
 * locations:
 *   countries:
 *     Russia:
 *       - city: Moscow
 *         lat: 55.7558
 *         lon: 37.6176
 *       - city: Saint Petersburg
 *         lat: 59.9343
 *         lon: 30.3351
 *     Germany:
 *       - city: Berlin
 *         lat: 52.5200
 *         lon: 13.4050
 *       - city: Hamburg
 *         lat: 53.5511
 *         lon: 9.9937
 * </pre>
 */

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
