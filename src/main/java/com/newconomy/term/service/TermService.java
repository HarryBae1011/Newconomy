package com.newconomy.term.service;

import com.newconomy.global.error.exception.handler.GeneralHandler;
import com.newconomy.global.response.status.ErrorStatus;
import com.newconomy.term.domain.Term;
import com.newconomy.term.dto.TermResponseDTO;
import com.newconomy.term.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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

    public List<TermResponseDTO.TermAutocompleteDTO> autocomplete(String keyword) {
        // keyword로 null이나 공백이 들어오면 빈 리스트 반환
        if (keyword == null || keyword.trim().isEmpty())
            return Collections.emptyList();

        // 자동완성 검색 개수는 최대 10개
        Pageable limit = PageRequest.of(0, 10);

        List<Term> terms = termRepository.searchByKeywordTop10(keyword.trim(), limit);

        return terms.stream()
                .map(t -> TermResponseDTO.TermAutocompleteDTO.builder()
                        .id(t.getId())
                        .name(t.getTermName())
                        .build())
                .toList();
    }
}
