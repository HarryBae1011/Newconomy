package com.newconomy.term.controller;

import com.newconomy.global.response.ApiResponse;
import com.newconomy.term.dto.TermResponseDTO;
import com.newconomy.term.service.TermService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
