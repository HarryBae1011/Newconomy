package com.newconomy.global.common;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class HtmlUtils {

    // HTML 태그 완전 제거
    public static String removeHtmlTags(String html) {
        return Jsoup.parse(html).text();
    }

    // 특정 태그만 허용
    public static String cleanHtml(String html) {
        return Jsoup.clean(html, Safelist.basic());
    }
}