package com.brt.duet.schedule.task;

/**
 * @author 方杰
 * @date 2019年10月14日
 * @description 
 */
public class MyRunnable implements Runnable {
	
	private String id;
	
	public MyRunnable(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void run() {
		System.out.println(id);
	}

}
