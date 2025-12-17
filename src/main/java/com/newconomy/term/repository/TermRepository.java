package com.newconomy.term.repository;

import com.newconomy.term.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermRepository extends JpaRepository<Term, Long> {
}
