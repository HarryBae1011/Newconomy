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
    STOCK("증권"),
    INDUSTRY("산업/재계"),
    VENTURE("중기/벤처"),
    REAL_ESTATE("부동산"),
    GLOBAL_ECONOMY("글로벌 경제"),
    LIFE("생활경제"),
    NORMAL("경제 일반");

    private final String displayName;

    public static NewsCategory toNewsCategory(String displayName) {
        return Arrays.stream(values())
                .filter(c -> c.displayName.equals(displayName))
                .findAny()
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.NEWS_CATEGORY_NOT_FOUND));
    }
}
