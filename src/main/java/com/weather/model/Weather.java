package com.weather.model;

import java.sql.Timestamp;

public record Weather(String сity, String country, Integer temperature, Timestamp date) { }
