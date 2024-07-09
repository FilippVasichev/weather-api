package com.weather.model;

import java.sql.Timestamp;

public record Weather(
        String name,
        Integer temperature,
        Timestamp date
        ) {}
