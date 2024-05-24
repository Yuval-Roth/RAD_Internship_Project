package com.arealcompany.ms_news.business.dtos;

import java.util.Map;

public record Article (
        String title,
        String description,
        String content,
        String url,
        String image,
        String publishedAt,
        Map<String,String> source
){}
