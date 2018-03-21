package org.crawler.processor;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;

public interface Processor {
	public void process(Page page, CrawlDatums next);
}
