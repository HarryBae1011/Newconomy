package com.newconomy.news.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "naver.news.rss")
@Getter
@Setter
public class NaverNewsProperties {

    private Map<String, String> economy;
}
