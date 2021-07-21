package ru.job4j.html;

import java.time.LocalDateTime;

public class Post {
    private int id;
    private String title;
    private String link;
    private String description;
    private LocalDateTime created;

    public Post(String title, String link, String description, LocalDateTime created) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.created = created;
    }

    @Override
    public String toString() {
        return "Post{"
                + "id=" + id
                + ", title='" + title + '\''
                + ", link='" + link + '\''
                + ", description='" + description + '\''
                + ", created=" + created
                + '}';
    }
}
