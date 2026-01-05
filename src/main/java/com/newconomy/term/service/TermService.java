package com.newconomy.term.service;

import com.newconomy.global.error.exception.handler.GeneralHandler;
import com.newconomy.global.response.status.ErrorStatus;
import com.newconomy.member.domain.Member;
import com.newconomy.member.domain.MemberTerm;
import com.newconomy.member.repository.MemberRepository;
import com.newconomy.member.repository.MemberTermRepository;
import com.newconomy.term.domain.Term;
import com.newconomy.term.dto.TermResponseDTO;
import com.newconomy.term.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TermService {

    private final TermRepository termRepository;
    private final MemberRepository memberRepository;
    private final MemberTermRepository memberTermRepository;

    public List<TermResponseDTO.SingleTermResultDTO> searchAllTerms() {
        List<Term> termList = termRepository.findAll();

        return termList.stream()
                .map(t -> TermResponseDTO.SingleTermResultDTO.builder()
                        .termId(t.getId())
                        .termName(t.getTermName())
                        .build()).toList();
    }

    @Transactional
    public Term getSingleTerm(Long memberId, Long termId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Term term = termRepository.findById(termId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.TERM_NOT_FOUND));

        boolean b = memberTermRepository.existsByMemberAndTerm(member, term);

        if (b) {
            MemberTerm memberTerm = memberTermRepository.findByMemberAndTerm(member, term)
                    .orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_TERM_NOT_FOUND));

            // 이미 조회한 적이 있는 용어라면 viewCount 증가, 최근 조회 일시 갱신
            memberTerm.updateTermStudy(LocalDateTime.now());
            memberTermRepository.save(memberTerm);
        } else {
            // 조회한 적이 없는 용어인 경우 MemberTerm 객체 생성
            MemberTerm built = MemberTerm.builder()
                    .member(member)
                    .term(term)
                    .masteryLevel(0)
                    .viewCount(0)
                    .isSaved(false)
                    .firstLearnedAt(LocalDateTime.now())
                    .lastReviewedAt(LocalDateTime.now())
                    .build();

            memberTermRepository.save(built);
        }

        return term;
    }

    public TermResponseDTO.BriefSingleTermDTO getBriefSingleTerm(Long termId) {
        Term term = termRepository.findById(termId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.TERM_NOT_FOUND));

        return TermResponseDTO.BriefSingleTermDTO.builder()
                .termId(term.getId())
                .termName(term.getTermName())
                .briefExplanation(term.getDetailedExplanation()) // 추후에 간단한 설명으로 바꿔야함
                .build();
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
