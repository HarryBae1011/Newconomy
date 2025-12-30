package com.newconomy.term.domain;

import com.newconomy.term.repository.TermRepository;
import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EconomicTermTrie {

    private Trie trie;
    private Map<String, Term> keywordToTermMap = new HashMap<>();

    public EconomicTermTrie(TermRepository termRepository) {
        List<Term> terms = termRepository.findAll();
        buildTrie(terms);
    }

    private void buildTrie(List<Term> terms) {
        Trie.TrieBuilder builder = Trie.builder()
                .ignoreCase()
                .onlyWholeWords();

        for (Term term : terms) {
            builder.addKeyword(term.getTermName());
            keywordToTermMap.put(term.getTermName(), term);
        }

        this.trie = builder.build();
    }

    public Collection<Emit> parse(String text) {
        // Trie에 등록된 모든 경제 용어를 대상으로 들어온 text 전체를 검색
        // 키워드 끝에 도달하면 (경제 용어가 검색되면) Emit 생성
        return trie.parseText(text);
    }

    public Term getMappedTerm(String keyword) {
        return keywordToTermMap.get(keyword);
    }
}
