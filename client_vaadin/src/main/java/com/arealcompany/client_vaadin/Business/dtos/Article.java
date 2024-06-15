package com.arealcompany.client_vaadin.Business.dtos;

import java.util.Map;

public record Article(
        String title,
        String description,
        String content,
        String url,
        String image,
        String publishedAt,
        Map<String,String> source
){
    public Article() {
        this("","","","","","",Map.of());
    }
}
