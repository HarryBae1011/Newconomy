package com.newconomy.term.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TermCategory {
    MONETARY("통화/금융"),        // 금리, 통화량, 중앙은행
    INVESTMENT("투자/증시"),      // 주식, 채권, 펀드
    REAL_ESTATE("부동산"),        // 아파트, 청약, 담보대출
    MACRO("거시경제"),            // GDP, 인플레이션, 경기순환
    MICRO("미시경제"),            // 수요/공급, 시장구조
    LIFE("생활금융");

    private String displayName;

/*    public static TermCategory toTermCategory(String displayName) {
    return Arrays.stream(values())
            .filter(c -> c.displayName.equals(displayName))
            .findAny()
            .orElseThrow(() -> new GeneralHandler(ErrorStatus.ITEM_CATEGORY_NOT_FOUND));
}*/
}
