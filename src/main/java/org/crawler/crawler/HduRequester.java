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
			request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
			request.setHeader("Host", "v.baidu.com");
			request.setHeader("Referer", "http://v.baidu.com/v");
			request.setCookie("BIDUPSID=165A3B8D1F93219D14C0B4A8138AEF6A; PSTM=1503732400; __cfduid=db0db3f162e9aee15f3eb36abaa675d1f1508568007; BAIDUID=17956543C27BB1FA4E9D7A458BC3F00C:FG=1; MCITY=-%3A; d_ad_beforeplay_today_num=6; H_PS_PSSID=1460_19033_21122; BDORZ=FFFB88E999055A3F8A630C64834BD6D0; BDRCVFR[X7WRLt7HYof]=mk3SLVN4HKm; PSINO=5; bdv_right_ad_poster=1; BUBBLESDEl=1");
			break;
		case ProcessorType.PROCESSOR_TYPE_BAIDU_PLAY:
			request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
			if(crawlDatum.meta("Referer") != null) {
				request.setHeader("Referer", crawlDatum.meta("Referer"));
			}
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
