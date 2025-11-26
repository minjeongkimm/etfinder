package com.etfinder.mvc.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public class DateUtil {

    private static final DateTimeFormatter F = DateTimeFormatter.BASIC_ISO_DATE; // yyyyMMdd

    public static String yyyymmdd(LocalDate d) {
        return d.format(F);
    }

    // API가 데이터 줄 때까지 어제→그제… 최대 7번 시도
    public static String findLatestBasDd(Function<String, Boolean> hasData) {
        LocalDate d = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            String basDd = yyyymmdd(d.minusDays(i));
            if (hasData.apply(basDd)) return basDd;
        }
        return yyyymmdd(LocalDate.now().minusDays(1));
    }
}
