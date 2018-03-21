package org.crawler.crawler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.crawler.constants.ProcessorType;
import org.crawler.listener.CrawlerBeginListener;
import org.crawler.listener.CrawlerEndListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.net.HttpRequest;
import cn.edu.hfut.dmic.webcollector.net.HttpResponse;
import cn.edu.hfut.dmic.webcollector.net.Requester;

@Component
public class HduRequester implements Requester, CrawlerBeginListener, CrawlerEndListener{
	private static final Logger logger = LoggerFactory.getLogger(HduRequester.class);
	private static final ExecutorService exec = Executors.newFixedThreadPool(1);
	private String detailCookie;
	private boolean flag = true;
	
	@Value("${crawler.machine}")
	private Integer machine;
	@Value("${crawler.proxy.enable}")
	private boolean proxyEnable;
	@Value("${crawler.requestDetail}")
	private boolean requestDetail;
	
	@Override
	public HttpResponse getResponse(CrawlDatum crawlDatum) throws Exception {
		HttpResponse res = null;
		HttpRequest request = null;
		try{
			request = new HttpRequest(crawlDatum);
			request.setMAX_REDIRECT(4);
			setHeader(crawlDatum, request);
			if(proxyEnable){
			}
			res = request.getResponse();
		}  catch (Exception e) {
			throw e;
		} finally {
		}
		return res;
	}

	private void setHeader(CrawlDatum crawlDatum, HttpRequest request) {
		
		switch (crawlDatum.meta(ProcessorType.PROCESSOR_TYPE)) {
		case ProcessorType.PROCESSOR_TYPE_BAIDU_SEARCH:
			break;
		default:
			break;
		}	
	}

	@Override
	public void crawlerBegin() {
		if(requestDetail) {
			exec.execute(new Runnable() {
				@Override
				public void run() {
				}
			});
			exec.shutdown();
		}
	}


	@Override
	public void crawlerEnd() {
		this.flag = false;
	}
	
}
