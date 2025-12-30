package com.newconomy.term.service;

import com.newconomy.global.error.exception.handler.GeneralHandler;
import com.newconomy.global.response.status.ErrorStatus;
import com.newconomy.news.domain.News;
import com.newconomy.news.domain.NewsTerm;
import com.newconomy.news.repository.NewsRepository;
import com.newconomy.news.repository.NewsTermRepository;
import com.newconomy.term.domain.EconomicTermTrie;
import com.newconomy.term.domain.Term;
import lombok.RequiredArgsConstructor;
import org.ahocorasick.trie.Emit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TermExtractService {

    private final EconomicTermTrie termTrie;
    private final NewsRepository newsRepository;
    private final NewsTermRepository newsTermRepository;

    @Transactional
    public List<NewsTerm> extract(Long newsId, String fullContent) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.NEWS_NOT_FOUND));

        // 기사 원문 특수기호 제거
        String normalized = normalizeText(fullContent);

        // 기사에서 경제 용어 검색
        Collection<Emit> emits = termTrie.parse(normalized);

        List<NewsTerm> newsTermList = new ArrayList<>();

        // 같은 위치 중복 저장 방지용 Set
        Set<String> seen = new HashSet<>();

        for (Emit emit : emits) {
            Term term = termTrie.getMappedTerm(emit.getKeyword());

            // 같은 위치 중복 저장 방지
            String dedupKey = term.getId() + ":" + emit.getStart();
            if (!seen.add(dedupKey)) continue;

            // 기사에서 DB에 존재하는 경제 용어 발견시 NewsTerm 객체 생성
            NewsTerm newsTerm = NewsTerm.builder()
                            .news(news)
                            .term(term)
                            .startIndex(emit.getStart())
                            .endIndex(emit.getEnd())
                            .build();

            newsTermList.add(newsTerm);
        }

        return newsTermRepository.saveAll(newsTermList);
    }

    private String normalizeText(String fullContent) {
        // 모든 특수문자를 공백 1개로 1대1로 치환 (원문과 index 정확히 일치하도록)
        return fullContent.replaceAll("[^가-힣a-zA-Z0-9%.\\s]", " ");
    }

}
