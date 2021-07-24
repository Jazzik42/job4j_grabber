package ru.job4j.grabber.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlRuDateTimeParser implements DateTimeParser {
    private static final Map<String, Integer> MONTHS = Map.ofEntries(
            Map.entry("янв", 1),
            Map.entry("фев", 2),
            Map.entry("мар", 3),
            Map.entry("апр", 4),
            Map.entry("май", 5),
            Map.entry("июн", 6),
            Map.entry("июл", 7),
            Map.entry("авг", 8),
            Map.entry("сен", 9),
            Map.entry("окт", 10),
            Map.entry("ноя", 11),
            Map.entry("дек", 12)
    );

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
        return MONTHS.get(month);
    }

    public static void main(String[] args) {
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        String a = "вчера, 20:33";
        System.out.println(parser.parse(a));
    }
}

