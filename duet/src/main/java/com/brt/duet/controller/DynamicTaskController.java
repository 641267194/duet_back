package com.brt.duet.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brt.duet.schedule.task.MyRunnable;
import com.brt.duet.util.IDUtil;

@RestController
@Component
public class DynamicTaskController {

	@Autowired
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;

	private Map<String,ScheduledFuture<?>> futures = new HashMap<String, ScheduledFuture<?>>();

	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		return new ThreadPoolTaskScheduler();
	}

	@RequestMapping("/startCron")
	public String startCron() {
		String id = IDUtil.getUUID();
		ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(new MyRunnable(id), new CronTrigger("0/5 * * * * *"));
		futures.put(id, future);
		return "startCron: " + id;
	}

	@RequestMapping("/stopCron")
	public String stopCron(@RequestParam("id") String id) {
		ScheduledFuture<?> future = futures.get(id);
		if (future != null) {
			future.cancel(true);
			futures.remove(id);
		}
		return "stopCron: " + id;
	}

	@RequestMapping("/changeCron10")
	public String startCron10(@RequestParam("id") String id) {
		stopCron(id);// 先停止，在开启.
		ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(new MyRunnable(id), new CronTrigger("*/10 * * * * *"));
		futures.put(id, future);
		return "changeCron10: " + id;
	}

}