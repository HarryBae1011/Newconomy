package com.newconomy.news.repository;

import com.newconomy.news.domain.News;
import com.newconomy.news.domain.NewsTerm;
import com.newconomy.term.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewsTermRepository extends JpaRepository<NewsTerm, Long> {

    @Query("select nt from NewsTerm nt join fetch nt.term where nt.news = :news")
    List<NewsTerm> findAllByNews(@Param("news") News news);

    boolean existsByNewsAndTerm(News news, Term term);
}
