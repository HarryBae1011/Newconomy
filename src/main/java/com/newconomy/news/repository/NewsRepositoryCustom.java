package com.newconomy.news.repository;

import com.newconomy.news.dto.NewsResponseDTO;
import com.newconomy.news.enums.NewsCategory;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsRepositoryCustom {
    List<NewsResponseDTO.SingleNewsDTO> searchNews(Pageable limit, NewsCategory newsCategory);
}
