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
		int size = 0;
		//System.out.println(page.getHtml());
		Elements elements1 = page.select(".result");
		for(Element element:elements1){
			Elements tmp = element.getElementsByAttributeValue("target", "_blank");
			if(tmp != null){
				Element aTag = tmp.first(); 
				String href = aTag.attr("href");
				System.out.println(href);
				size++;
			}
		}
		//result-op
		Elements elements2 = page.select(".result-op");
		for(Element element:elements2){
			Elements tmp = element.getElementsByAttributeValue("target", "_blank");
			if(tmp != null){
				Element aTag = tmp.first(); 
				String href = aTag.attr("href");
				System.out.println(href);
				size++;
			}
		}
		System.out.println("共搜索到" + size + "条数据");
	}

}
