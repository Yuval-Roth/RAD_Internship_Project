package com.arealcompany.ms_population.business.dtos;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.lang.Nullable;

@Document(value = "population_stats")
public record PopulationStat(Long count, String readable_format, @MongoId String country) {

    public PopulationStat(Long count, String readable_format, @Nullable String country) {
        this.count = count;
        this.readable_format = readable_format;
        this.country = country == null ? "global" : country.toLowerCase();
    }
}
