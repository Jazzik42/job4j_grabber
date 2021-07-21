package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SqlRuParse implements Parse {
    private final SqlRuDateTimeParser dateParser;

    public SqlRuParse(SqlRuDateTimeParser dateParser) {
        this.dateParser = dateParser;
    }

    @Override
    public List<Post> list(String link) throws IOException {
        List<Post> list = new ArrayList<>();
        Document doc;
        for (int i = 1; i < 6; i++) {
            doc = Jsoup.connect(link + i).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                String postUrl = href.attr("href");
                list.add(detail(postUrl));
            }
        }
        return list;
    }

    @Override
    public Post detail(String link) throws IOException {
        Document page = Jsoup.connect(link).get();
        Post post = null;
        if (!(Stream.of("table[class=msgTable]", "td[class=messageHeader]",
                "td[class=msgFooter]").allMatch(s -> page.select(s).isEmpty()))) {
            Element page1 = page.select("table[class=msgTable]").first();
            String titleText = page1.select("td[class=messageHeader]").first().text();
            String date = page1.select("td[class=msgFooter]").first().text().split(" \\[")[0];
            String descriptionText = page1.select("td[class=msgBody]").last().text();

            post = new Post(titleText, descriptionText, link,
                    dateParser.parse(date));
        }
        return post;
    }

    public static void main(String[] args) {
        SqlRuDateTimeParser dateParser = new SqlRuDateTimeParser();
        SqlRuParse sqlRuParse = new SqlRuParse(dateParser);
        try {
            List<Post> list = sqlRuParse.list("https://www.sql.ru/forum/job-offers/");
            for (Post post : list) {
                System.out.println(post);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
