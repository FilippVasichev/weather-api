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

    public Weather insert(
            String city,
            String country,
            int temperature,
            Timestamp date
    ) {
        return dslContext.insertInto(WEATHER)
                .set(WEATHER.CITY, city)
                .set(WEATHER.COUNTRY, country)
                .set(WEATHER.TEMPERATURE, temperature)
                .set(WEATHER.DATE, date.toLocalDateTime().toLocalDate())
                .returning()
                .fetchOne()
                .map(record -> new Weather(
                                record.get(WEATHER.ID),
                                record.get(WEATHER.CITY),
                                record.get(WEATHER.COUNTRY),
                                record.get(WEATHER.TEMPERATURE),
                                record.get(WEATHER.DATE)
                        )
                );
    }

    public List<Weather> getAllWeather(){
        return dslContext.selectFrom(WEATHER).fetchInto(Weather.class);
    }
}
