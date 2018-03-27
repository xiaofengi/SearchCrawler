package org.crawler.main;

import java.util.concurrent.atomic.AtomicLong;

import org.crawler.crawler.HduCrawler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(basePackages = { "org.crawler" })
@PropertySource(value = "classpath:crawler.properties")
public class HduStarter {
	
	public static AtomicLong liSize = new AtomicLong(0L);
	public static AtomicLong baidusize = new AtomicLong(0L);
	public static AtomicLong eastDaySize = new AtomicLong(0L);
	public static AtomicLong cctvSize = new AtomicLong(0L);
	public static AtomicLong ku6Size = new AtomicLong(0L);

	@SuppressWarnings("resource")
	public static void main(String[] args) throws InterruptedException {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(HduStarter.class);
		HduCrawler baiduCrawler = applicationContext.getBean(HduCrawler.class);
		baiduCrawler.start();
	}
}
