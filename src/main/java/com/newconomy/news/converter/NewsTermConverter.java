package com.newconomy.news.converter;

import com.newconomy.news.domain.News;
import com.newconomy.news.domain.NewsTerm;
import com.newconomy.news.dto.NewsResponseDTO;
import com.newconomy.term.domain.Term;

public class NewsTermConverter {
    public static NewsTerm toNewsTerm(NewsResponseDTO.NewsTermGenerateDTO dto, Term term, News news){
        return NewsTerm.builder().term(term)
                .news(news)
                .startIndex(dto.getStartIndex())
                .endIndex(dto.getEndIndex())
                .contextSentence(dto.getContextSentence())
                .build();
    }
}
