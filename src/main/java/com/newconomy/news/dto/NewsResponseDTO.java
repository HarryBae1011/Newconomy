package com.newconomy.news.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.newconomy.news.enums.NewsCategory;
import com.newconomy.term.enums.TermCategory;
import lombok.*;

import java.time.LocalDateTime;
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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SingleNewsViewDTO {
        private Long newsId;
        private String title;
        private String fullContent;
        private String newsImgUrl;
        private NewsCategory newsCategory;
        private String source;
        private LocalDateTime publishedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewsTermGenerateListDTO{
        @JsonProperty("terms")
        List<NewsTermGenerateDTO> newsTermGenerateList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewsTermGenerateDTO{
        String termName;
        String simpleExplanation;
        String detailedExplanation;
        TermCategory termCategory;
        int difficultyLevel;
        int startIndex;
        int endIndex;
        String contextSentence;
    }

}
