package com.weather.storage;

import com.weather.tables.pojos.Weather;

import static com.weather.tables.Weather.WEATHER;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;


@Repository
public class WeatherStorage {
    private final DSLContext dslContext;

    public WeatherStorage(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public void insert(String city, String country, int temperature, Timestamp date) {
        dslContext.insertInto(WEATHER)
                .set(WEATHER.CITY, city)
                .set(WEATHER.COUNTRY, country)
                .set(WEATHER.TEMPERATURE, temperature)
                .set(WEATHER.DATE, date.toLocalDateTime().withSecond(0).withNano(0))
                .execute();
    }

    public List<Weather> getAllWeather() {
        return dslContext.selectFrom(WEATHER).fetchInto(Weather.class);
    }

    public Weather getCityWeatherByDate(String country, String city, Timestamp date) {
        return dslContext.selectFrom(WEATHER)
                .where(WEATHER.COUNTRY.eq(country))
                .and(WEATHER.CITY.eq(city))
                .and(WEATHER.DATE.eq(date.toLocalDateTime().withSecond(0).withNano(0)))
                .fetchOne(record -> new Weather(
                                record.get(WEATHER.ID),
                                record.get(WEATHER.CITY),
                                record.get(WEATHER.COUNTRY),
                                record.get(WEATHER.TEMPERATURE),
                                record.get(WEATHER.DATE)
                        )
                );
    }

    public Weather getCityWeatherWithoutDate(String country, String city) {
        return dslContext.selectFrom(WEATHER)
                .where(WEATHER.COUNTRY.eq(country))
                .and(WEATHER.CITY.eq(city))
                .orderBy(WEATHER.DATE.desc())
                .limit(1)
                .fetchOne(record -> new Weather(
                                record.get(WEATHER.ID),
                                record.get(WEATHER.CITY),
                                record.get(WEATHER.COUNTRY),
                                record.get(WEATHER.TEMPERATURE),
                                record.get(WEATHER.DATE)
                        )
                );
    }
}
