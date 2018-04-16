package org.crawler.processor.baidu;

import org.crawler.processor.Processor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;

@Component
public class BaiduSearchProcessor implements Processor{

	@Override
	public void process(Page page, CrawlDatums next) {
		Elements elements = page.select(".result c-container");
		for(Element element:elements){
			
		}
	}

}
