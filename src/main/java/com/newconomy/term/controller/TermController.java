package com.newconomy.term.controller;

import com.newconomy.global.response.ApiResponse;
import com.newconomy.term.domain.Term;
import com.newconomy.term.dto.TermResponseDTO;
import com.newconomy.term.service.TermService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "경제 용어 컨트롤러")
@RestController
@RequestMapping("/api/term")
@RequiredArgsConstructor
public class TermController {

    private final TermService termService;

    @GetMapping
    @Operation(summary = "전체 경제 용어 목록 조회", description = "모든 경제 용어 목록을 조회하는 API")
    public ApiResponse<TermResponseDTO.TermResultListDTO> viewAllTerms() {
        List<TermResponseDTO.SingleTermResultDTO> singleTermResultDTOList = termService.searchAllTerms();
        return ApiResponse.onSuccess(
                TermResponseDTO.TermResultListDTO.builder()
                .terms(singleTermResultDTOList)
                .build());
    }

    @GetMapping("/{termId}")
    @Operation(summary = "특정 경제 용어 상세 조회", description = "경제 용어를 상세 조회하는 API")
    public ApiResponse<TermResponseDTO.SingleTermDTO> viewSingleTerm(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long termId) {
        Term term = termService.getSingleTerm(memberId, termId);
        return ApiResponse.onSuccess(
                TermResponseDTO.SingleTermDTO.builder()
                        .termId(term.getId())
                        .termName(term.getTermName())
                        .detailedExplanation(term.getDetailedExplanation())
                        .build());
    }

    @GetMapping("/autocomplete")
    @Operation(summary = "경제 용어 자동완성 검색", description = "입력한 키워드로 경제 용어를 검색하는 API")
    public ApiResponse<TermResponseDTO.TermAutocompleteListDTO> autoComplete(
            @RequestParam @Parameter(description = "검색 키워드") String keyword) {
        List<TermResponseDTO.TermAutocompleteDTO> autocomplete = termService.autocomplete(keyword);
        return ApiResponse.onSuccess(
                TermResponseDTO.TermAutocompleteListDTO.builder()
                        .terms(autocomplete)
                        .build());
    }
}
