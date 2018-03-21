package org.crawler.main;

import org.crawler.crawler.HduCrawler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(basePackages = { "org.crawler" })
@PropertySource(value = "classpath:crawler.properties")
public class HduStarter {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws InterruptedException {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(HduStarter.class);
		HduCrawler baiduCrawler = applicationContext.getBean(HduCrawler.class);
		baiduCrawler.start();
	}
}
