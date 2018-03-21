package org.crawler.crawler;

import java.util.List;

import org.crawler.constants.DatumConstants;
import org.crawler.constants.ProcessorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;

@Component
public class DatumGenerator {
	private static final Logger logger = LoggerFactory.getLogger(DatumGenerator.class);
	
	public CrawlDatum generateKeyword(String keyword) {
		return new CrawlDatum(String.format(DatumConstants.BADIDU_SEARCH_URL, keyword))
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_BAIDU_SEARCH);
	}

	public CrawlDatum generatePlayPage(String url) {
		return new CrawlDatum(url)
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_BAIDU_PLAY);
	}
}