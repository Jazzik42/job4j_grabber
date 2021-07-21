package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.util.stream.Stream;

public class SqlRuParse {

    public static Post init(String url) throws IOException {
        Document page = Jsoup.connect(url).get();
        Post post = null;
        if (!(Stream.of("table[class=msgTable]", "td[class=messageHeader]",
                "td[class=msgFooter]").allMatch(s -> page.select(s).isEmpty()))) {
            Element page1 = page.select("table[class=msgTable]").first();
            String titleText = page1.select("td[class=messageHeader]").first().text();
            String date = page1.select("td[class=msgFooter]").first().text().split(" \\[")[0];
            String descriptionText = page1.select("td[class=msgBody]").last().text();
            SqlRuDateTimeParser dateD = new SqlRuDateTimeParser();
            post = new Post(titleText, descriptionText, url, dateD.parse(date));
        }
        return post;
    }

    public static void main(String[] args) throws Exception {
        Document doc;
        for (int i = 1; i < 6; i++) {
            doc = Jsoup.connect("https://www.sql.ru/forum/job-offers/" + i).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                Element el = td.parent().child(5);
                System.out.println(href.attr("href"));
                System.out.println(href.text());
                System.out.println(el.text());
            }
            System.out.println("------------------------------------------------------");
        }
    }
}
