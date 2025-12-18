package com.newconomy.news.repository;

import com.newconomy.news.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long>, NewsRepositoryCustom {
}
