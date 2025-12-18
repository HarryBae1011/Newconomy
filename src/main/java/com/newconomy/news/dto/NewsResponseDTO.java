package com.newconomy.news.dto;

import lombok.*;

import java.util.List;

public class NewsResponseDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewsItemsResponseDTO {
        private String lastBuildDate;
        private int total;
        private int start;
        private int display;
        private List<SingleNewsItemDTO> items;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SingleNewsItemDTO {
        private String title;
        private String originallink;
        private String link;
        private String description;
        private String pubDate;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewsListViewDTO {
        private List<SingleNewsDTO> newsDTOList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SingleNewsDTO {
        private Long newsId;
        private String title;
        private String url;
        private String originalUrl;
    }
}
