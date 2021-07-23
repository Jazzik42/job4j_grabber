package ru.job4j.grabber;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class SqlSchema {
    private Properties properties;

    private void propertiesInit() {
        properties = new Properties();
        try (InputStream io = SqlSchema.class.getClassLoader()
                .getResourceAsStream("app.properties")) {
            properties.load(io);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        propertiesInit();
        Class.forName(properties.getProperty("driver_class"));
        String url = properties.getProperty("url");
        String password = properties.getProperty("password");
        String userName = properties.getProperty("name");
        return DriverManager.getConnection(url, userName, password);
    }

    public void initSchema() {
        try (Connection con = getConnection(); PreparedStatement st = con.prepareStatement("create table ? (?, ?, ?, ?, ?)")) {
            st.setString(1, "post");
            st.setString(2, "id primary key");
            st.setString(3, "name text");
            st.setString(4, "text text");
            st.setString(5, "link text not null unique");
            st.setString(5, "created timestamp");
            st.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
