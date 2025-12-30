package com.newconomy.news.repository;

import com.newconomy.news.domain.NewsTerm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsTermRepository extends JpaRepository<NewsTerm, Long> {
}
