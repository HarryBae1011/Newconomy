package com.newconomy.news.crawling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newconomy.news.dto.NewsResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Component
public class SearchClient {

    @Value(value = "${naver.client.id}")
    private String clientId = "7Nri3DZ8RknLZ2IL8SWO";

    @Value(value = "${naver.client.secret}")
    private String clientSecret;

    private static final String API_URL = "https://openapi.naver.com/v1/search/news.json?query=";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public NewsResponseDTO.NewsItemsResponseDTO searchNews(String query, Integer display) {
        try {
            String encoded = URLEncoder.encode(query, "UTF-8");
            URL url = new URL(API_URL + encoded + "&display=" + display);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

            int status = con.getResponseCode();
            BufferedReader br;

            if (status == HttpURLConnection.HTTP_OK) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
            con.disconnect();

            return objectMapper.readValue(sb.toString(), NewsResponseDTO.NewsItemsResponseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("네이버 API 호출 실패", e);
        }
    }
}
