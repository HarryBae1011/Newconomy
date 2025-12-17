package com.newconomy.term.service;

import com.newconomy.global.error.exception.handler.GeneralHandler;
import com.newconomy.global.response.status.ErrorStatus;
import com.newconomy.term.domain.Term;
import com.newconomy.term.dto.TermResponseDTO;
import com.newconomy.term.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TermService {

    private final TermRepository termRepository;

    public List<TermResponseDTO.SingleTermResultDTO> searchAllTerms() {
        List<Term> termList = termRepository.findAll();

        return termList.stream()
                .map(t -> TermResponseDTO.SingleTermResultDTO.builder()
                        .termId(t.getId())
                        .termName(t.getTermName())
                        .build()).toList();
    }

    public Term getSingleTerm(Long termId) {
        return termRepository.findById(termId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.TERM_NOT_FOUND));
    }
}
