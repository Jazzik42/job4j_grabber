package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.quartz.*;

import java.io.IOException;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class QuartzGrab implements Grab {

    @Override
    public void init(Parse parse, Store store, Scheduler scheduler, String url) throws SchedulerException {
        try {
            scheduler.start();
            JobDataMap map = new JobDataMap();
            map.put("store", store);
            map.put("parse", parse);
            map.put("url", url);
            JobDetail jobDetail = newJob(Parser.class)
                    .usingJobData(map)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(20)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

    public static class Parser implements Job {

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            Parse parse = (Parse) jobExecutionContext.getJobDetail().getJobDataMap().get("parse");
            Store store = (Store) jobExecutionContext.getJobDetail().getJobDataMap().get("store");
            String url = (String) jobExecutionContext.getJobDetail().getJobDataMap().get("url");
            try {
                for (Post post : parse.list(url)) {
                    store.save(post);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
