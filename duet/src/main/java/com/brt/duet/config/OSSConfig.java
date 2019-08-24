package com.brt.duet.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.brt.duet.util.OSSClientUtil;

/**
 * @author 方杰
 * @date 2019年7月22日
 * @description 阿里OSS配置类
 */
@Configuration
public class OSSConfig {

	@Bean
	@ConfigurationProperties(prefix = "oss")
	public OSSClientUtil getOSSClientUtil() {
		return new OSSClientUtil();
	}

}
