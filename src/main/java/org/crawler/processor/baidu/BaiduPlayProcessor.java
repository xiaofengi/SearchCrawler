package org.crawler.processor.baidu;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.crawler.constants.DatumConstants;
import org.crawler.crawler.DatumGenerator;
import org.crawler.main.HduStarter;
import org.crawler.processor.Processor;
import org.crawler.util.DownloadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;

@Component
public class BaiduPlayProcessor implements Processor{
	
	private static Logger logger = LoggerFactory.getLogger(BaiduPlayProcessor.class); 
	
	@Resource
	private DatumGenerator datumGenerator;

	@Override
	public void process(Page page, CrawlDatums next) {
		if(page.matchUrl("http://baishi\\.baidu\\.com/watch/.*")){ //百度视频
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
			String fileName = DatumConstants.DOWNLOAD_LOC_BAIDU + downloadUrl.substring(downloadUrl.indexOf("com/")+4, downloadUrl.indexOf("?"));
			boolean success = DownloadUtil.download(downloadUrl,  fileName, 3600, null);
			System.out.println("下载");
			System.out.println(downloadUrl);
			if(success) {
				System.out.println("成功");
			}else {
				System.out.println("失败");
			}
			HduStarter.baiduSize.getAndIncrement();
		}else if(page.matchUrl("http://baishi\\.baidu\\.com/link.*")){ //百度视频重定向其他视频网站
			String redirectUrl = page.select("#link").get(0).attr("href");
			logger.info(page.getUrl()+"重定向到" + redirectUrl);
			next.add(datumGenerator.generateRedirectPlayPage(redirectUrl, page.getUrl()));
		}else if (page.matchUrl("http://video\\.eastday\\.com.*")) { //东方头条视频		
			int start = page.getHtml().indexOf("mp4 = ");
			if(start == -1) {
				logger.info("fail:" + page.getUrl());
				return;
			}
			String tmp = page.getHtml().substring(start+7, start+100);
			int end = tmp.indexOf("mp4\";");
			String downloadUrl = "http:" + tmp.substring(0, end+3);
			String fileName = DatumConstants.DOWNLOAD_LOC_EASTDAY + downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
			boolean success = DownloadUtil.download(downloadUrl,  fileName, 3600, page.getUrl());
			System.out.println("下载");
			System.out.println(downloadUrl);
			if(success) {
				System.out.println("成功");
			}else {
				System.out.println("失败");
			}
			HduStarter.eastDaySize.getAndIncrement();
		}else if (page.matchUrl("http://xiyou\\.cctv\\.com/v-.*")) {//cctv视频播放页面，不是视频地址
			String videoId = page.getUrl().substring(page.getUrl().indexOf("v-")+2, page.getUrl().indexOf(".html"));
			//爬取cctv视频地址信息接口
			next.add(datumGenerator.generateCCTVVideo(videoId, page.getUrl()));
		}else if(page.matchUrl("http://xiyou\\.cctv\\.com/interface/index\\?videoId=.*")) {//cctv视频地址信息接口
			Map videoInfo = new Gson().fromJson(page.getHtml(), Map.class);
			List<Map> data = (List<Map>)videoInfo.get("data");
			List<Map> videoList = (List<Map>)data.get(0).get("videoList");
			Map videoFilePaths = videoList.get(0);
			if(videoFilePaths.containsKey("videoFilePath")) {//标清视频
				String tmp = ((String)videoFilePaths.get("videoFilePath")).split("#")[0];
				String videoFlag = tmp.substring(tmp.lastIndexOf("/")+1);
				String videoFilePath;
				String fileName;
				if(videoFlag.contains(page.meta("videoId"))) {
					videoFilePath = tmp + "_001.mp4";
					fileName = DatumConstants.DOWNLOAD_LOC_CCTV + videoFlag + "_001.mp4";
				}else {
					videoFilePath = tmp + ".mp4";
					fileName = DatumConstants.DOWNLOAD_LOC_CCTV + videoFlag + ".mp4";
				}
				boolean success = DownloadUtil.download(videoFilePath,  fileName, 3600, page.getUrl());
				System.out.println("下载");
				System.out.println(videoFilePath);
				if(success) {
					System.out.println("成功");
				}else {
					System.out.println("失败");
				}
				HduStarter.cctvSize.getAndIncrement();
			}
			if(videoFilePaths.containsKey("videoFilePathHD")) {//高清视频
				String tmp = ((String)videoFilePaths.get("videoFilePathHD")).split("#")[0];
				String videoFlag = tmp.substring(tmp.lastIndexOf("/")+1);
				String videoFilePathHD;
				String fileName;
				if(videoFlag.contains(page.meta("videoId"))) {
					videoFilePathHD = tmp + "_001.mp4";
					fileName = DatumConstants.DOWNLOAD_LOC_CCTV + videoFlag + "_001.mp4";
				}else {
					videoFilePathHD = tmp + ".mp4";
					fileName = DatumConstants.DOWNLOAD_LOC_CCTV + videoFlag + ".mp4";
				}
				boolean success = DownloadUtil.download(videoFilePathHD,  fileName, 3600, page.getUrl());
				System.out.println("下载");
				System.out.println(videoFilePathHD);
				if(success) {
					System.out.println("成功");
				}else {
					System.out.println("失败");
				}
				HduStarter.cctvSize.getAndIncrement();
			}
			if(videoFilePaths.containsKey("videoFilePathSHD")) {//超清视频
				String tmp = ((String)videoFilePaths.get("videoFilePathSHD")).split("#")[0];
				String videoFlag = tmp.substring(tmp.lastIndexOf("/")+1);
				String videoFilePathSHD;
				String fileName;
				if(videoFlag.contains(page.meta("videoId"))) {
					videoFilePathSHD = tmp + "_001.mp4";
					fileName = DatumConstants.DOWNLOAD_LOC_CCTV + videoFlag + "_001.mp4";
				}else {
					videoFilePathSHD = tmp + ".mp4";
					fileName = DatumConstants.DOWNLOAD_LOC_CCTV + videoFlag + ".mp4";
				}
				boolean success = DownloadUtil.download(videoFilePathSHD,  fileName, 3600, page.getUrl());
				System.out.println("下载");
				System.out.println(videoFilePathSHD);
				if(success) {
					System.out.println("成功");
				}else {
					System.out.println("失败");
				}
				HduStarter.cctvSize.getAndIncrement();
			}
		}else { //其他视频网站暂不处理
			logger.info("fail:" + page.getUrl());
		}
	}

}
