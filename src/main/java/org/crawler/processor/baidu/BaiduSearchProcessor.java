package org.crawler.processor.baidu;

import javax.annotation.Resource;
import org.crawler.crawler.DatumGenerator;
import org.crawler.processor.Processor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;

@Component
public class BaiduSearchProcessor implements Processor{
	
	@Resource
	private DatumGenerator datumGenerator;

	@Override
	public void process(Page page, CrawlDatums next) {

		Elements lis = page.select(".result");
		for(Element li : lis) {
			String playVideoUrl = li.getElementsByTag("a").get(0).attr("href");
			if(playVideoUrl.charAt(0) == '/') {//东方头条视频加refer字段
				playVideoUrl = "http://baishi.baidu.com" + playVideoUrl;
			}
			next.add(datumGenerator.generatePlayPage(playVideoUrl));
			System.out.println(playVideoUrl);
		}
		System.out.println(page.getUrl() + " - 搜索到" + lis.size() + "个视频");
		//爬取下一页
		int pn = Integer.parseInt(page.meta("pn"));
		if(pn > 1000) {
			return;
		}
		if(pn == 0 ) {
			pn = 60;
		}else {
			pn += 20;
		}
		next.add(datumGenerator.generateVideoList(page.meta("keyword"), pn));
	}

}
