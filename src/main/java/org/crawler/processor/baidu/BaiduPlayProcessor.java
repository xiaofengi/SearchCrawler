package org.crawler.processor.baidu;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.annotation.Resource;
import org.crawler.crawler.DatumGenerator;
import org.crawler.main.HduStarter;
import org.crawler.processor.Processor;
import org.crawler.util.DownloadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;

@Component
public class BaiduPlayProcessor implements Processor{
	
	private static Logger logger = LoggerFactory.getLogger(BaiduPlayProcessor.class); 
	
	@Resource
	private DatumGenerator datumGenerator;

	@Override
	public void process(Page page, CrawlDatums next) {
		if(page.matchUrl("http://baishi.baidu.com/watch/.*")){ //百度视频
			int start = page.getHtml().indexOf("video=http");
			if(start == -1) {
				logger.info("fail:" + page.getUrl());
				return;
			}
			String tmp = page.getHtml().substring(start+6, start+500);
			int end = tmp.indexOf("';");
			String downloadUrl = tmp.substring(0, end);
			try {
				downloadUrl = URLDecoder.decode(downloadUrl, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String fileName = "E:\\Downloads\\crawler\\baidu\\baidu\\" + downloadUrl.substring(downloadUrl.indexOf("com/")+4, downloadUrl.indexOf("?"));
			boolean success = DownloadUtil.download(downloadUrl,  fileName, 3600, null);
			System.out.println("下载");
			System.out.println(downloadUrl);
			if(success) {
				System.out.println("成功");
			}else {
				System.out.println("失败");
			}
		}else if(page.matchUrl("http://baishi.baidu.com/link.*")){ //百度视频重定向其他视频网站
			String redirectUrl = page.select("#link").get(0).attr("href");
			logger.info(page.getUrl()+"重定向到" + redirectUrl);
			next.add(datumGenerator.generateRedirectPlayPage(redirectUrl, page.getUrl()));
		}else if (page.matchUrl("http://video.eastday.com.*")) { //东方头条视频		
			int start = page.getHtml().indexOf("mp4 = ");
			if(start == -1) {
				logger.info("fail:" + page.getUrl());
				return;
			}
			HduStarter.eastDaySize.getAndIncrement();
			String tmp = page.getHtml().substring(start+7, start+100);
			int end = tmp.indexOf("mp4\";");
			String downloadUrl = "http:" + tmp.substring(0, end+3);
			String fileName = "E:\\Downloads\\crawler\\baidu\\eastday\\" + downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
			boolean success = DownloadUtil.download(downloadUrl,  fileName, 3600, page.getUrl());
			System.out.println("下载");
			System.out.println(downloadUrl);
			if(success) {
				System.out.println("成功");
			}else {
				System.out.println("失败");
			}
		}else { //其他视频网站暂不处理
			logger.info("fail:" + page.getUrl());
		}
	}

}
