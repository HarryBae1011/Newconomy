package com.newconomy.news.controller;

import com.newconomy.global.response.ApiResponse;
import com.newconomy.news.crawling.SearchService;
import com.newconomy.news.dto.NewsResponseDTO;
import com.newconomy.news.enums.NewsCategory;
import com.newconomy.news.service.NewsService;
import com.newconomy.news.service.NewsTermGenerateService;
import com.newconomy.term.dto.TermResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "뉴스 컨트롤러")
@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final SearchService searchService;
    private final NewsService newsService;
    private final NewsTermGenerateService newsTermGenerateService;

    @GetMapping("/search")
    @Operation(summary = "경제 뉴스 검색", description = "경제 뉴스를 크롤링 해오는 API 입니다.")
    public ApiResponse<NewsResponseDTO.NewsItemsResponseDTO> searchNews(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") Integer display) {
        NewsResponseDTO.NewsItemsResponseDTO newsItemsResponseDTO = searchService.searchNews(query, display);
        return ApiResponse.onSuccess(newsItemsResponseDTO);
    }

    @GetMapping
    @Operation(summary = "경제 뉴스 목록 조회", description = "경제 뉴스 목록 조회 API, 뉴스 카테고리별로 조회 가능")
    public ApiResponse<NewsResponseDTO.NewsListViewDTO> viewAllNews(
            @RequestParam(value = "newsCategory", required = false) String category,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "12") int size
    ) {
        // 뉴스 카테고리 필터링이 들어왔는지 확인
        NewsCategory newsCategory = category != null
                ? NewsCategory.toNewsCategory(category.trim())
                : NewsCategory.MAIN;

        List<NewsResponseDTO.SingleNewsDTO> singleNewsDTOList = newsService.viewNews(newsCategory, PageRequest.of(page, size));
        return ApiResponse.onSuccess(
                NewsResponseDTO.NewsListViewDTO.builder()
                        .newsDTOList(singleNewsDTOList)
                        .build());
    }

    @GetMapping("/{newsId}")
    @Operation(summary = "단일 뉴스 상세 조회", description = "단일 뉴스 상세 조회 API")
    public ApiResponse<NewsResponseDTO.SingleNewsViewDTO> viewSingleNews(
            @PathVariable("newsId") Long newsId) {
        NewsResponseDTO.SingleNewsViewDTO singleNewsViewDTO = newsService.viewSingleNews(newsId);
        return ApiResponse.onSuccess(singleNewsViewDTO);
    }

    @GetMapping("/headline")
    @Operation(summary = "네이버 경제 뉴스 헤드라인 검색", description = "네이버 경제 뉴스 헤드라인을 검색후 각 기사의 원문까지 크롤링하여 저장, 경제 뉴스 하위 카테고리별로도 검색 가능")
    public ApiResponse<NewsResponseDTO.NewsListViewDTO> searchHeadLineNews(
            @RequestParam(value = "newsCategory", required = false) String newsCategory
    ) {
        List<NewsResponseDTO.SingleNewsDTO> singleNewsDTOList = newsService.searchHeadLineNews(newsCategory);
        return ApiResponse.onSuccess(
                NewsResponseDTO.NewsListViewDTO.builder()
                        .newsDTOList(singleNewsDTOList)
                        .build());
    }

    @GetMapping("/{newsId}/term")
    @Operation(summary = "뉴스 경제 용어 목록 조회", description = "뉴스 기사 원문에 등장한 경제 용어 목록 조회 API")
    public ApiResponse<TermResponseDTO.TermResultListDTO> viewNewsTerms(
            @PathVariable("newsId") Long newsId
    ) {
        NewsResponseDTO.NewsTermGenerateListDTO newsTermGenerateListDTO = newsTermGenerateService.generateNewsTerm(newsId);

        List<TermResponseDTO.SingleTermResultDTO> singleTermResultDTOList = newsService.viewNewsTerms(newsId);
        return ApiResponse.onSuccess(
                TermResponseDTO.TermResultListDTO.builder()
                        .terms(singleTermResultDTOList)
                        .build());
    }
}
