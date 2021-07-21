package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PostParser implements Parse {
    private final SqlRuDateTimeParser dateParser;

    public PostParser(SqlRuDateTimeParser dateParser) {
        this.dateParser = dateParser;
    }

    @Override
    public List<Post> list(String link) throws IOException {
        List<Post> list = new ArrayList<>();
        Document doc = Jsoup.connect(link).get();
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element href = td.child(0);
            String postUrl = href.attr("href");
            list.add(detail(postUrl));
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

            post = new Post(titleText, link, descriptionText,
                    dateParser.parse(date));
        }
        return post;
    }
}
