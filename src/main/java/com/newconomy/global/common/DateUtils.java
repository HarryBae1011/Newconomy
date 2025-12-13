package com.newconomy.global.common;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class DateUtils {

    private static final DateTimeFormatter RFC_1123_FORMATTER =
            DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

    public static LocalDateTime parseRFC1123(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }

        try {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString, RFC_1123_FORMATTER);
            return zonedDateTime.toLocalDateTime();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("날짜 파싱 실패: " + dateString, e);
        }
    }
}
