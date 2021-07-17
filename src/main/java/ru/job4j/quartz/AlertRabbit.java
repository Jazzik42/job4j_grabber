package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {

    private static Connection con;
    private static Properties prop;
    private static int interval;

    private static void init() throws SQLException, ClassNotFoundException {
        Class.forName(prop.getProperty("hibernate.connection.driver_class"));
        String url = prop.getProperty("hibernate.connection.url");
        String username = prop.getProperty("hibernate.connection.username");
        String password = prop.getProperty("hibernate.connection.password");
        con = DriverManager.getConnection(url, username, password);
        interval = Integer.parseInt(prop.getProperty("rabbit.interval"));
    }

    private static void propertiesRead() {
        try (InputStream in = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            prop = new Properties();
            prop.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            propertiesRead();
            init();
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            String createTable = String.format("create table rabbit(%s);",
                    "created_date timestamp");
            JobDataMap data = new JobDataMap();
            data.put("connection", con);
            data.put("create", createTable);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(interval)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(5000);
            scheduler.shutdown();
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    public static class Rabbit implements Job {

        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
            Connection connection = (Connection) context.getJobDetail().getJobDataMap().get("connection");
            try {
                String insertData = (String.format("insert into rabbit(%s) values ('%s');",
                        "created_date", Timestamp.valueOf(
                                LocalDateTime.now().withNano(0))));
                String createTable = (String) context.getJobDetail().getJobDataMap().get("create");
                Statement a = connection.createStatement();
                a.execute(createTable);
                a.execute(insertData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

