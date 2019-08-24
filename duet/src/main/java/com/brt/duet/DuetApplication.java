package com.brt.duet;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.brt.duet.dao"})
public class DuetApplication {

	public static void main(String[] args) {
		SpringApplication.run(DuetApplication.class, args);
	}
}
