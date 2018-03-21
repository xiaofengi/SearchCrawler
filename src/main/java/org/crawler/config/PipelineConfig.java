package org.crawler.config;

import org.crawler.pipeline.Pipeline;
import org.crawler.pipeline.impl.BaiduMysqlPipeline;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PipelineConfig {
	@Value("${crawler.pipeline.type:file}")
	private String pipelineType;
	
	@Bean
	public Pipeline pipeline(){
		switch (pipelineType) {
		case "baidu_mysql":
			return new BaiduMysqlPipeline();
		default:
			break;
		}
		return null;
	}
}
