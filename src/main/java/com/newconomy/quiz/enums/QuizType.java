package com.newconomy.quiz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuizType {
    MULTIPLE_CHOICE("객관식"),
    OX("OX퀴즈");

    private String displayName;

/*    public static QuizType toQuizType(String displayName) {
    return Arrays.stream(values())
            .filter(c -> c.displayName.equals(displayName))
            .findAny()
            .orElseThrow(() -> new GeneralHandler(ErrorStatus.ITEM_CATEGORY_NOT_FOUND));
}*/
}
