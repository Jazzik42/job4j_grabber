package ru.job4j.grabber;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {
    private Connection con;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("driver_class"));
            String url = cfg.getProperty("url");
            String user = cfg.getProperty("username");
            String password = cfg.getProperty("password");
            con = DriverManager.getConnection(url, user, password);
            initSchema();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void initSchema() {
        String sql = String.format("create table post (id serial primary key, %s",
                "title text, link text not null unique, text text, created timestamp);");
        try (Statement st = con.createStatement()) {
            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement pStatement = con.prepareStatement("insert into post (title, link, text, created) values (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            Timestamp created = Timestamp.valueOf(post.getCreated());
            pStatement.setString(1, post.getTitle());
            pStatement.setString(2, post.getLink());
            pStatement.setString(3, post.getDescription());
            pStatement.setTimestamp(4, created);
            pStatement.execute();
            try (ResultSet resultKey = pStatement.getGeneratedKeys()) {
                if (resultKey.next()) {
                    post.setId(resultKey.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> list = new ArrayList<>();
        try (PreparedStatement pStatement = con.prepareStatement("select * from post");
             ResultSet resultSet = pStatement.executeQuery()) {
            while (resultSet.next()) {
                list.add(new Post(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getTimestamp(5).toLocalDateTime()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Post findById(int id) {
        Post post = null;
        try (PreparedStatement pStatement = con.prepareStatement("select * from post where id = ?")
        ) {
            pStatement.setInt(1, id);
            try (ResultSet resultSet = pStatement.executeQuery()) {
                if (resultSet.next()) {
                    post = new Post(resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getTimestamp(5).toLocalDateTime());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public void close() throws Exception {
        if (con != null) {
            con.close();
        }
    }

    public static void main(String[] args) {
        Properties properties = new Properties();
        try (InputStream io = PsqlStore.class.getClassLoader().getResourceAsStream("test.properties")) {
            properties.load(io);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PsqlStore psqlStore = new PsqlStore(properties);
        LocalDateTime lc = LocalDateTime.now();
        psqlStore.save(new Post("1", "1", "1", lc));
        LocalDateTime lc2 = LocalDateTime.now();
        psqlStore.save(new Post("2", "2", "2", lc2));
        System.out.println(psqlStore.getAll());
        System.out.println(psqlStore.findById(2));
    }
}
