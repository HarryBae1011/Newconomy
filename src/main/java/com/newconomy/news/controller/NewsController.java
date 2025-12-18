package com.newconomy.news.controller;

import com.newconomy.global.response.ApiResponse;
import com.newconomy.news.crawling.SearchService;
import com.newconomy.news.dto.NewsResponseDTO;
import com.newconomy.news.enums.NewsCategory;
import com.newconomy.news.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "뉴스 컨트롤러")
@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final SearchService searchService;
    private final NewsService newsService;

    @GetMapping("/search")
    @Operation(summary = "경제 뉴스 검색", description = "경제 뉴스를 크롤링 해오는 API 입니다.")
    public ApiResponse<NewsResponseDTO.NewsItemsResponseDTO> searchNews(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") Integer display) {
        NewsResponseDTO.NewsItemsResponseDTO newsItemsResponseDTO = searchService.searchNews(query, display);
        return ApiResponse.onSuccess(newsItemsResponseDTO);
    }

    @GetMapping
    @Operation(summary = "경제 뉴스 조회", description = "경제 뉴스 목록 조회 API, 뉴스 카테고리별로 조회 가능")
    public ApiResponse<NewsResponseDTO.NewsListViewDTO> viewAllNews(
            @RequestParam(value = "newsCategory", required = false) String category
    ) {
        // 뉴스 카테고리 필터링이 들어왔는지 확인
        NewsCategory newsCategory = category != null
                ? NewsCategory.toNewsCategory(category)
                : null;

        List<NewsResponseDTO.SingleNewsDTO> singleNewsDTOList = newsService.viewNews(newsCategory);
        return ApiResponse.onSuccess(
                NewsResponseDTO.NewsListViewDTO.builder()
                        .newsDTOList(singleNewsDTOList)
                        .build());
    }
}
