package com.newconomy.newconomy.news.controller;

import com.newconomy.news.domain.News;
import com.newconomy.news.dto.NewsResponseDTO;
import com.newconomy.news.enums.NewsCategory;
import com.newconomy.news.repository.NewsRepository;
import com.newconomy.news.repository.NewsTermRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsTermRepository newsTermRepository;

    @MockitoBean
    private WebClient webClient;
    private WebClient.RequestBodyUriSpec requestBodyUriSpec = org.mockito.Mockito.mock(WebClient.RequestBodyUriSpec.class);
    private WebClient.RequestBodySpec requestBodySpec = org.mockito.Mockito.mock(WebClient.RequestBodySpec.class);
    private WebClient.RequestHeadersSpec requestHeadersSpec = org.mockito.Mockito.mock(WebClient.RequestHeadersSpec.class);
    private WebClient.ResponseSpec responseSpec = org.mockito.Mockito.mock(WebClient.ResponseSpec.class);

    private News testNews;

    @BeforeEach
    void setting(){
        String detailedContent = "최근 글로벌 금융 시장의 변동성이 커지면서 투자자들 사이에서 안정적인 현금 흐름을 창출할 수 있는 '배당소득'에 대한 관심이 그 어느 때보다 높아지고 있습니다. "
                + "전통적인 주식 투자 방식에서 벗어나 분기별 혹은 월별로 지급되는 배당금은 하락장에서 훌륭한 방어 기제로 작용하기 때문입니다. "
                + "금융당국 관계자는 배당 절차 개선안을 통해 투자자들이 배당금을 먼저 확인하고 투자할 수 있는 환경을 조성하겠다고 밝혔습니다. "
                + "한편, 해외 주식 투자 비중이 높은 개인 투자자들 사이에서는 'RIA (Return to Investment Account)' 계좌를 활용한 절세 전략이 주목받고 있습니다. "
                + "RIA 계좌는 해외 주식을 매각한 대금을 국내로 들여오지 않고 해당 계좌 안에서 즉시 재투자할 수 있도록 돕는 시스템으로, 환전 수수료 절감 및 과세 이연 효과를 기대할 수 있는 것이 특징입니다. "
                + "전문가들은 이러한 경제 용어들을 정확히 이해하는 것이 복잡한 거시 경제 흐름 속에서 자신의 자산을 지키는 첫걸음이라고 조언합니다. "
                + "본 뉴스는 이러한 트렌드를 반영하여 최신 금융 기법과 세제 혜택 정보를 지속적으로 전달할 예정입니다. "
                + "앞으로도 시장의 변화에 민감하게 반응하며 투자자들에게 실질적인 도움이 되는 정보를 제공하는 데 최선을 다하겠습니다.";
        News news = News.builder()
                .title("글로벌 금융 시장 변동성에 따른 배당소득 및 RIA 계좌 활용 전략")
                .content("adsfasf")
                .fullContent(detailedContent)
                .publishedAt(LocalDateTime.now())
                .newsCategory(NewsCategory.FINANCE)
                .crawledAt(LocalDateTime.now())
                .originalUrl("http://www")
                .source("adsfas")
                .url("http://www")
                .build();
        testNews = newsRepository.save(news);
    }

    @AfterEach
    void tearDown() {
        // @Transactional을 뺐으므로 직접 데이터를 지워줘야 다음 테스트에 영향을 안 줍니다.
        newsTermRepository.deleteAll();
        newsRepository.deleteAll();
    }

    @Test
    @WithMockUser
    @DisplayName("용어 생성 요청 후, polling을 통해 모든 용어가 한번에 조회되는지 test")
    void generateTerm() throws Exception{
        var term1 = NewsResponseDTO.NewsTermGenerateDTO.builder()
                .termName("배당소득")
                .simpleExplanation("주식 투자로 받는 배당금에 대한 소득")
                .build();
        var term2 = NewsResponseDTO.NewsTermGenerateDTO.builder()
                .termName("RIA (Return to Investment Account)")
                .simpleExplanation("해외 주식을 매각하고 국내 주식에 재투자하는 계좌")
                .build();
        var llmResponse = NewsResponseDTO.NewsTermGenerateListDTO.builder()
                .newsTermGenerateList(List.of(term1, term2))
                        .build();

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/api/news-term/generate")).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(NewsResponseDTO.NewsTermGenerateListDTO.class))
                .thenReturn(Mono.just(llmResponse));

        mockMvc.perform(post("/api/news/{newsId}/generateTerm", testNews.getId())
                        .with(csrf())) // POST에는 CSRF 토큰이 필요합니다.
                .andExpect(status().isOk());

        await().atMost(10, TimeUnit.SECONDS)
                .pollInterval(2,TimeUnit.SECONDS)
                .untilAsserted(() ->
                        mockMvc.perform(get("/api/news/{newsId}/term", testNews.getId())
                                        .with(user("user")))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.result.terms", hasSize((2)))));

    }
}
