package com.newconomy.term.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class TermResponseDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TermResultListDTO {
        private List<SingleTermResultDTO> terms;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SingleTermResultDTO {
        private Long termId;
        private String termName;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SingleTermDTO {
        private Long termId;
        private String termName;
        private String detailedExplanation;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TermAutocompleteListDTO {
        private List<TermAutocompleteDTO> terms;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TermAutocompleteDTO {
        private Long id;
        private String name;
    }
}
