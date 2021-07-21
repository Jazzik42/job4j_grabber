package ru.job4j.grabber;

import java.util.ArrayList;
import java.util.List;

public class MemStore implements Store {
    private final List<Post> listPost = new ArrayList<>();
    private int id = 1;

    @Override
    public void save(Post post) {
        listPost.add(post);
        post.setId(id++);
    }

    @Override
    public List<Post> getAll() {
        return listPost;
    }

    @Override
    public Post findById(int id) {
        Post post = null;
        for (Post post1 : listPost) {
            if (post1.getId() == id) {
                post = post1;
            }
        }
        return post;
    }

    private int indexOf(int id) {
        int index = -1;
        for (Post post : listPost) {
            if (post.getId() == id) {
                index = 1;
                break;
            }
        }
        return index;
    }
}
