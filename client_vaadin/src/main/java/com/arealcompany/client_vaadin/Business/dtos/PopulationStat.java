package com.arealcompany.client_vaadin.Business.dtos;

import org.springframework.lang.Nullable;

public record PopulationStat(Long count, String readable_format, @Nullable String country) {

    public PopulationStat(Long count, String readable_format, @Nullable String country) {
        this.count = count;
        this.readable_format = readable_format;
        this.country = country == null ? "global" : country.toLowerCase();
    }
}
