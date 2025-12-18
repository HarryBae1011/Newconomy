package com.newconomy.news.service;

import com.newconomy.global.response.ApiResponse;
import com.newconomy.news.dto.NewsResponseDTO;
import com.newconomy.news.enums.NewsCategory;
import com.newconomy.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    public List<NewsResponseDTO.SingleNewsDTO> viewNews(NewsCategory newsCategory) {
        Pageable limit =  PageRequest.of(0, 10);

        return newsRepository.searchNews(limit, newsCategory);
    }
}
