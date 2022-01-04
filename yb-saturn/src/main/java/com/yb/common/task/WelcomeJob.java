package com.yb.common.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class WelcomeJob implements Job {


    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try {
            System.out.println("===============开始获取数据==============");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}