package com.brt.duet.schedule;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class DemoScheduleTask {
	
	//3.添加定时任务
    @Scheduled(cron = "0 5 0 0/1 * ?")
	//@Scheduled(cron = "0/10 * * * * ?")
    private void configureTasks() {
    	System.out.println("这是定时任务");
    }
}
