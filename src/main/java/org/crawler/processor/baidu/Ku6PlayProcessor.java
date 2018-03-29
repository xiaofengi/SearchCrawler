package org.crawler.processor.baidu;

import org.crawler.constants.DatumConstants;
import org.crawler.main.HduStarter;
import org.crawler.processor.Processor;
import org.crawler.util.DownloadUtil;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;

@Component
public class Ku6PlayProcessor implements Processor{
	
	private static Logger logger = LoggerFactory.getLogger(Ku6PlayProcessor.class);

	@Override
	public void process(Page page, CrawlDatums next) {
		if(page.matchUrl("https://.*\\.ku6\\.com.*")){//酷6视频地址
			if(page.getHtml().contains("flvURL")) {
				String tmp = page.getHtml().substring(page.getHtml().indexOf("flvURL"), page.getHtml().indexOf("flvURL")+100);
				String flvURL = tmp.substring(tmp.indexOf("http"), tmp.indexOf("\","));
				String fileName = DatumConstants.DOWNLOAD_LOC_KU6 + flvURL.substring(flvURL.lastIndexOf("/")+1);
				if(!fileName.contains(".")) {
					fileName = fileName+".mp4";
				}
				boolean success = DownloadUtil.download(flvURL,  fileName, 3600, page.getUrl());
				System.out.println("下载");
				System.out.println(flvURL);
				if(success) {
					System.out.println("成功");
				}else {
					System.out.println("失败");
				}
				HduStarter.ku6Size.getAndIncrement();
			}
			if(page.getHtml().contains("playUrl")) {
				String tmp = page.getHtml().substring(page.getHtml().indexOf("playUrl"), page.getHtml().indexOf("playUrl")+100);
				String playUrl = tmp.substring(tmp.indexOf("http"), tmp.indexOf("\","));
				String fileName = DatumConstants.DOWNLOAD_LOC_KU6 + playUrl.substring(playUrl.lastIndexOf("/")+1);
				if(!fileName.contains(".")) {
					fileName = fileName+".mp4";
				}
				boolean success = DownloadUtil.download(playUrl,  fileName, 3600, page.getUrl());
				System.out.println("下载");
				System.out.println(playUrl);
				if(success) {
					System.out.println("成功");
				}else {
					System.out.println("失败");
				}
				HduStarter.ku6Size.getAndIncrement();
			}
			if(page.select("video").first() != null) {
				Element video = page.select("video").first();
				Element source = video.getElementsByTag("source").first();
				if(source == null) {
					return;
				}
				String src = source.attr("src");
				String fileName = DatumConstants.DOWNLOAD_LOC_KU6 + src.substring(src.lastIndexOf("/")+1);
				if(!fileName.contains(".")) {
					fileName = fileName+".mp4";
				}
				boolean success = DownloadUtil.download(src,  fileName, 3600, page.getUrl());
				System.out.println("下载");
				System.out.println(src);
				if(success) {
					System.out.println("成功");
				}else {
					System.out.println("失败");
				}
				HduStarter.ku6Size.getAndIncrement();
			}
		}
	} 	

}
