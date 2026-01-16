package com.newconomy.term.repository;

import com.newconomy.term.domain.Term;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface TermRepository extends JpaRepository<Term, Long> {

    @Query("select t from Term t where t.termName like %:keyword% order by t.termName")
    List<Term> searchByKeywordTop10(@Param("keyword") String keyword, Pageable pageable);

    Optional<Term> findByTermName(String termName);
}
