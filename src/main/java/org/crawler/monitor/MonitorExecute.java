package org.crawler.monitor;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.crawler.listener.CrawlerBeginListener;
import org.crawler.listener.CrawlerEndListener;
import org.crawler.util.MonitorInfoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MonitorExecute implements CrawlerBeginListener,CrawlerEndListener{

	private static final Logger logger = LoggerFactory.getLogger(MonitorExecute.class);
	
	@Value("${crawler.monitor.active}")
	private boolean active;//是否启用监控
	
	@Value("${crawler.monitor.interval}")
	private int interval;//间隔时间
	
	public static final AtomicLong saveCounter = new AtomicLong(0);
	public static final AtomicLong counter = new AtomicLong(0);
	public static final AtomicInteger fileCounter = new AtomicInteger(0);
	public static final AtomicLong soldCounter = new AtomicLong(0);
	
	@Override
	public void crawlerBegin() {//爬虫开始时 开始调用监控 
		if(!active){
			return; //测试代码不启动监控
		}
		
		MonitorThread startThread = new MonitorThread(0,getMsgParam());
		startThread.start();
		
		MonitorThread mot = new MonitorThread(interval); //运行
		MonitorThread.flag = true;
		logger.info("---inspectorStart---");
		mot.start();
	}

	@Override
	public void crawlerEnd() {//爬虫结束时调用日报接口
		if(!active){
			return; //测试代码不启动监控
		}
		MonitorThread.flag = false;//停止定时调用
	
		MonitorThread mot = new MonitorThread(2,getMsgParam());
		mot.start();
		
		MonitorThread mot2 = new MonitorThread(3,getDailyParam());
		logger.info("---call dailyInspector---");
		mot2.start();
	}
	
	public void sendErrorMsg(String exceptionMsg){//发生异常时调用
		if(!active){
			return; 
		}
		MonitorParam monitorParam = new MonitorParam(exceptionMsg);
		MonitorThread mot = new MonitorThread(4,monitorParam);
		mot.start();
	}
	
	public static MonitorParam getMsgParam(){
		String cpu = MonitorInfoUtil.getCpuMsg();
		String ram = MonitorInfoUtil.getMemMsg();
		return new MonitorParam(counter.get(), saveCounter.get(), cpu, ram);
	}
	
	public MonitorParam getDailyParam(){
		Long totalCount = counter.get();
		Long totalSales = soldCounter.get();
		return new MonitorParam(totalCount,totalSales);
	}
	
}
