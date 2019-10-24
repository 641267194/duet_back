package com.brt.duet.util;

import java.util.UUID;

public class IDUtil {
	public static String getUUID(){
		String date = DateUtil.getYmdhmss();
		String uuid = UUID.randomUUID().toString().replace("-", "");
        return (date + uuid).substring(0, 36);
	}
	
	public static void main(String[] args) {
		for(int i = 0; i < 20; i ++) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(getUUID());
		}
		
	}
}
