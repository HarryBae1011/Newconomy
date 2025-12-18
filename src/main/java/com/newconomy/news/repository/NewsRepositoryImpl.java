package com.newconomy.news.repository;

import com.newconomy.news.domain.News;
import com.newconomy.news.dto.NewsResponseDTO;
import com.newconomy.news.enums.NewsCategory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.newconomy.news.domain.QNews.*;

public class NewsRepositoryImpl implements NewsRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public NewsRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<NewsResponseDTO.SingleNewsDTO> searchNews(Pageable pageable, NewsCategory newsCategory) {
        List<News> result = queryFactory.selectFrom(news)
                .where(
                        categoryEq(newsCategory)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return result.stream()
                .map(r -> NewsResponseDTO.SingleNewsDTO.builder()
                        .newsId(r.getId())
                        .title(r.getTitle())
                        .url(r.getUrl())
                        .originalUrl(r.getOriginalUrl())
                        .build()
                ).toList();
    }

    private BooleanExpression categoryEq(NewsCategory newsCategory) {
        return newsCategory != null ? news.newsCategory.eq(newsCategory) : null;
    }
}
