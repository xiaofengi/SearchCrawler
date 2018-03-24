package org.crawler.crawler;

import org.crawler.constants.DatumConstants;
import org.crawler.constants.ProcessorType;
import org.springframework.stereotype.Component;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;

@Component
public class DatumGenerator {
	//private static final Logger logger = LoggerFactory.getLogger(DatumGenerator.class);
	
	public CrawlDatum generateKeyword(String keyword) {
		return new CrawlDatum(String.format(DatumConstants.BADIDU_SEARCH_URL, keyword))
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_BAIDU_SEARCH)
				.meta("pn", "0");
	}
	
	public CrawlDatum generateVideoList(String keyword, int pn, int sc) {
		return new CrawlDatum(String.format(DatumConstants.BADIDU_SEARCH_URL, keyword, pn, sc))
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_BAIDU_SEARCH)
				.meta("keyword", keyword)
				.meta("pn", String.valueOf(pn))
				.meta("sc", String.valueOf(sc));
	}

	public CrawlDatum generatePlayPage(String url) {
		return new CrawlDatum(url)
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_BAIDU_PLAY);
	}

	public CrawlDatum generateRedirectPlayPage(String redirectUrl, String url) {
		return new CrawlDatum(redirectUrl)
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_BAIDU_PLAY)
				.meta("Referer", url);
	}
}