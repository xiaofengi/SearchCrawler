package org.crawler.main;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;
import org.crawler.crawler.HduCrawler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(basePackages = { "org.crawler" })
@PropertySource(value = "classpath:crawler.properties")
public class HduStarter {
	
	public static AtomicLong liSize = new AtomicLong(0L);
	public static AtomicLong baiduSize = new AtomicLong(0L);
	public static AtomicLong eastDaySize = new AtomicLong(0L);
	public static AtomicLong cctvSize = new AtomicLong(0L);
	public static AtomicLong ku6Size = new AtomicLong(0L);
	public static AtomicLong youtubeSize = new AtomicLong(0L);
	public static AtomicLong baiduSearchSize = new AtomicLong(0L);

	@SuppressWarnings("resource")
	public static void main(String[] args) throws InterruptedException {
		//设置系统代理
		/*Properties properties = System.getProperties();
		properties.setProperty("http.proxyHost", "127.0.0.1");
		properties.setProperty("http.proxyPort", "1080");
		properties.setProperty("https.proxyHost", "127.0.0.1");
		properties.setProperty("https.proxyPort", "1080");*/
		//程序入口
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(HduStarter.class);
		HduCrawler hduCrawler = applicationContext.getBean(HduCrawler.class);
		hduCrawler.start();
	}
}
