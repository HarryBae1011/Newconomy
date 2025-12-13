package com.newconomy.news.enums;

import com.newconomy.global.error.exception.handler.GeneralHandler;
import com.newconomy.global.response.status.ErrorStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum NewsCategory {
    FINANCE("금융"),
    REAL_ESTATE("부동산"),
    BUSINESS("기업/산업"),
    ECONOMY("경제정책"),
    LIFE("생활경제");

    private final String displayName;

    public static NewsCategory toNewsCategory(String displayName) {
        return Arrays.stream(values())
                .filter(c -> c.displayName.equals(displayName))
                .findAny()
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.NEWS_CATEGORY_NOT_FOUND));
    }
}
