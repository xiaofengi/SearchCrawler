package org.crawler.crawler;

import java.util.Map;
import org.crawler.listener.CrawlerBeginListener;
import org.crawler.listener.CrawlerEndListener;
import org.crawler.processor.manager.ProcessorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpResponse;
import cn.edu.hfut.dmic.webcollector.net.Requester;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

@Component
public class HduCrawler extends BreadthCrawler implements ApplicationContextAware{
	private static final Logger logger = LoggerFactory.getLogger(HduCrawler.class);
	
	@Value("${crawler.webcollector.depth}")
	private int depth;
	
	@Value("${crawler.webcollector.threads}")
	private int threads; 
	
	@Value("${crawler.webcollector.resumable}")
	private boolean resumable;
	
	@Value("${crawler.webcollector.topN}")
	private int topN;
	
	@Value("${crawler.webcollector.executeInterval}")
	private int executeInterval;
	
	@Autowired
	private Requester hduRequester;
	
	@Autowired
	private SeedGenerator seedGenerator;
	
	@Autowired
	private ProcessorManager processorManager;
	
	private Map<String, CrawlerEndListener> crawlerEndListenerMap;
	private Map<String, CrawlerBeginListener> crawlerbeginListenerMap;
	
	public HduCrawler(@Value("${crawler.webcollector.crawlPath}") String crawlPath, @Value("${crawler.webcollector.autoParse}") boolean autoParse) {
		super(crawlPath, autoParse);
	}
	
	/* (non-Javadoc)
	 * @see cn.edu.hfut.dmic.webcollector.fetcher.Visitor#visit(cn.edu.hfut.dmic.webcollector.model.Page, cn.edu.hfut.dmic.webcollector.model.CrawlDatums)
	 */
	public void visit(Page page, CrawlDatums next) {
		processorManager.process(page, next);
	}

	@Override
	public void execute(CrawlDatum datum, CrawlDatums next) throws Exception {
		HttpResponse response = requester.getResponse(datum);
        Page page = new Page(datum, response);
        visitor.visit(page, next);
	}
	
	public void start() {
		seedGenerator.addSeed(this);
		
		this.setRequester(hduRequester);
		logger.info("crawler start");
		notifyBeginCrawler();
		logger.info("request start");
		try {
			setResumable(resumable);
			setTopN(topN);
			setThreads(threads);
        	setExecuteInterval(executeInterval);
			start(depth);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		logger.info("request end");
        notifyEndCrawler();
        logger.info("crawler end" );
	}
	
	private void notifyBeginCrawler() {
		if(crawlerbeginListenerMap == null || crawlerbeginListenerMap.isEmpty()) {
			return;
		}
		for(CrawlerBeginListener listener: crawlerbeginListenerMap.values()) {
			listener.crawlerBegin();
		}
 	}

	private void notifyEndCrawler() {
		if(crawlerEndListenerMap == null || crawlerEndListenerMap.isEmpty()) {
			return;
		}
		
		if(crawlerEndListenerMap.containsKey("pipeline")) {
			crawlerEndListenerMap.get("pipeline").crawlerEnd();
		}
		
		for(CrawlerEndListener listener: crawlerEndListenerMap.values()) {
			listener.crawlerEnd();
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		crawlerbeginListenerMap = applicationContext.getBeansOfType(CrawlerBeginListener.class);
		crawlerEndListenerMap = applicationContext.getBeansOfType(CrawlerEndListener.class);
	}
}