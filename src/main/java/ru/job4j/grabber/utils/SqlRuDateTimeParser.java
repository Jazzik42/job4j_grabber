package ru.job4j.grabber.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlRuDateTimeParser implements DateTimeParser {
    private static final Map<String, Integer> MONTHS = new HashMap<>();

    @Override
    public LocalDateTime parse(String parse) {
        String[] dates = parse.split(", ");
        int month;
        Pattern pattern = Pattern.compile("\\D+");
        Matcher matcher = pattern.matcher(dates[0]);
        LocalDate localDate = LocalDate.now().minusDays(1);
        LocalTime localTime = LocalTime.parse(dates[1]);
        if (matcher.find()) {
            if (matcher.group().equals("сегодня")) {
                localDate = LocalDate.now();
            }
        } else {
            String[] dateWithYear = dates[0].split("\\s");
            month = getMonth(dateWithYear[1]);
            localDate = LocalDate.of(Integer.parseInt(dateWithYear[2]),
                    month, Integer.parseInt(dateWithYear[0]));
        }
        return LocalDateTime.of(localDate, localTime);
    }

    private static int getMonth(String month) {
        MONTHS.put("янв", 1);
        MONTHS.put("фев", 2);
        MONTHS.put("мар", 3);
        MONTHS.put("апр", 4);
        MONTHS.put("май", 5);
        MONTHS.put("июн", 6);
        MONTHS.put("июл", 7);
        MONTHS.put("авг", 8);
        MONTHS.put("сен", 9);
        MONTHS.put("окт", 10);
        MONTHS.put("ноя", 11);
        MONTHS.put("дек", 12);
        return MONTHS.get(month);
    }

    public static void main(String[] args) {
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        String a = "вчера, 20:33";
        System.out.println(parser.parse(a));
    }
}

