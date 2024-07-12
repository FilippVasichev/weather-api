package com.weather.config;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

public class JooqConfig {
    private final DataSource dataSource;

    public JooqConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public final DSLContext dslContext() {
        return DSL.using(dataSource, SQLDialect.POSTGRES);
    }
}
