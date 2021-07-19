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
        Map<String, Integer> months = new HashMap<>();
        months.put("янв", 1);
        months.put("фев", 2);
        months.put("мар", 3);
        months.put("апр", 4);
        months.put("май", 5);
        months.put("июн", 6);
        months.put("июл", 7);
        months.put("авг", 8);
        months.put("сен", 9);
        months.put("окт", 10);
        months.put("ноя", 11);
        months.put("дек", 12);
        return months.get(month);
    }

    public static void main(String[] args) {
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        String a = "вчера, 20:33";
        System.out.println(parser.parse(a));
    }
}

