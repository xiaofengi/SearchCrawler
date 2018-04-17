package org.crawler.processor.baidu;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;

import java.util.Date;

import javax.annotation.Resource;

import org.crawler.mysql.mapper.WebPageDetailMapper;
import org.crawler.mysql.model.WebPageDetail;
import org.crawler.processor.Processor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class BaiduSearchRsProcessor implements Processor{
	
	@Resource
	private WebPageDetailMapper webPageDetailMapper;

    @Override
    public void process(Page page, CrawlDatums next) {
        System.out.println(page.getResponse().getRealUrl());
        if("http://fund.eastmoney.com/161631.html?spm=aladin".equals(page.getResponse().getRealUrl().toString())){
        	System.out.println(page.getHtml());
        	Elements heads = page.select("head");
        	Elements titles = page.select("head").first().getElementsByTag("title");
        	String title = page.select("head").first().getElementsByTag("title").first().text();
        }
        //过滤与关键字不相关的网页
        String title = page.select("head").first().getElementsByTag("title").first().text();
        System.out.println(title);
        if(title.contains(page.meta("keyword"))){
	        WebPageDetail webPageDetail = new WebPageDetail();
	        webPageDetail.setUrl(page.getResponse().getRealUrl().toString());
	        webPageDetail.setDomain(page.getResponse().getRealUrl().getHost());
	        webPageDetail.setTitle(title);
	        webPageDetail.setSrc(page.getUrl());
	        webPageDetail.setKeyword(page.meta("keyword"));
	        webPageDetail.setHtml(page.getHtml());
	        webPageDetail.setCrawlTime(new Date());
	        webPageDetailMapper.insertSelective(webPageDetail);
        }
    }
}
