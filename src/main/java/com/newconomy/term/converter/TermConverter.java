package com.newconomy.term.converter;

import com.newconomy.news.dto.NewsResponseDTO;
import com.newconomy.term.domain.Term;

public class TermConverter {

    public static Term toTerm(NewsResponseDTO.NewsTermGenerateDTO request){
        return Term.builder()
                .termName(request.getTermName())
                .simpleExplanation(request.getSimpleExplanation())
                .detailedExplanation(request.getDetailedExplanation())
                .difficultyLevel(request.getDifficultyLevel())
                .termCategory(request.getTermCategory())
                .build();
    }
}

