package com.newconomy.news.controller;

import com.newconomy.global.response.ApiResponse;
import com.newconomy.news.crawling.SearchService;
import com.newconomy.news.dto.NewsResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final SearchService searchService;

    @GetMapping("/news")
    @Operation(summary = "경제 뉴스 검색", description = "경제 뉴스를 크롤링 해오는 API 입니다.")
    public ApiResponse<NewsResponseDTO.NewsItemsResponseDTO> searchNews(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") Integer display) {
        NewsResponseDTO.NewsItemsResponseDTO newsItemsResponseDTO = searchService.searchNews(query, display);
        return ApiResponse.onSuccess(newsItemsResponseDTO);
    }
}
