package org.crawler.processor.baidu;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.crawler.crawler.DatumGenerator;
import org.crawler.main.HduStarter;
import org.crawler.mysql.mapper.UrlRelationMapper;
import org.crawler.mysql.mapper.WebPageDetailMapper;
import org.crawler.mysql.model.UrlRelation;
import org.crawler.mysql.model.WebPageDetail;
import org.crawler.processor.Processor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class BaiduSearchRsProcessor implements Processor{

	private static final Logger logger = LoggerFactory.getLogger(BaiduSearchRsProcessor.class);
	@Resource
	private DatumGenerator datumGenerator;
	@Resource
	private WebPageDetailMapper webPageDetailMapper;
	@Resource
	private UrlRelationMapper urlRelationMapper;

    @Override
    public void process(Page page, CrawlDatums next) {
        //System.out.println(page.getResponse().getRealUrl());
        if(page.getResponse().getRealUrl().toString().contains("http://www.baidu.com/s")){ //过滤再次链接到百度搜索的网页
			return;
		}
		if(page.select("title").isEmpty()){
        	return;
		}
		parseWebPageDetail(page);
		parseWebSource(page, next);
		paerseHref(page, next);
    }

	/**
	 * 解析网页详情
	 * @param page
	 * @param next
	 */
	private void parseWebPageDetail(Page page){
		String title = page.select("title").first().text();
		//System.out.println(title);
		if(title.contains(page.meta("keyword"))){ //过滤与关键字不相关的网页
			WebPageDetail webPageDetail = new WebPageDetail();
			//网页地址
			webPageDetail.setUrl(page.getResponse().getRealUrl().toString());
			//域名
			webPageDetail.setDomain(page.getResponse().getRealUrl().getHost());
			//标题
			webPageDetail.setTitle(title);
			//源
			if(page.meta("referer") != null){ //超链接到当前页面
				webPageDetail.setSrc(page.meta("referer"));
			}else { //重定向到当前页面
				webPageDetail.setSrc(page.getUrl());
			}
			//创建时间
			String createTimeStr = null;
			if(!page.select(".time, .utime, .time").isEmpty()){
				createTimeStr = page.select(".time, .utime, .time").first().text();
			}
			if(createTimeStr != null){
				if(createTimeStr.matches("^\\d{4}年\\d{2}月\\d{2}日 \\d{2}:\\d{2}.*")){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
					Date createTime = null;
					try {
						createTime = sdf.parse(createTimeStr);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					webPageDetail.setCreateTime(createTime);
				}else if(createTimeStr.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}.*")) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					try {
						Date createTime = sdf.parse(createTimeStr);
						webPageDetail.setCreateTime(createTime);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
			//作者
			String author = null;
			if(!page.select(".author, .author-name, .from").isEmpty()){
				author = page.select(".author, .author-name, .from").first().text();
			}else if(!page.select("#user_info").isEmpty()){
				Element userInfo = page.select(".user_info").first();
				if(userInfo.getElementById("uid") != null) {
					author = userInfo.getElementById("uid").text();
				}
			}
			if(author != null){
				webPageDetail.setAuthor(author);
			}
			//关键字
			webPageDetail.setKeyword(page.meta("keyword"));
			//内容
			String content = null;
			if(!page.select("article").isEmpty()){
				content = page.select("article").first().text();
			}
			if(!page.select(".article_content, .topic-content, .main-content, .article-content-wrap, .sec_article").isEmpty()){
				content = page.select(".article_content, .topic-content, .main-content, .article-content-wrap, .sec_article").first().text();
			}
			if(content != null){
				webPageDetail.setContent(content);
			}
			//html
			webPageDetail.setHtml(page.getHtml());
			//浏览数
			Integer viewNum = null;
			if(!page.select(".art_click_count").isEmpty()){
				if(!StringUtils.isBlank(page.select(".art_click_count").first().text())) {
					viewNum = Integer.parseInt(page.select(".art_click_count").first().text());
				}
			}else if(!page.select(".icon-read").isEmpty()){
				String txt = page.select(".icon-read").first().nextElementSibling().text();
				if(!StringUtils.isBlank(txt)) {
					viewNum = Integer.parseInt(txt);
				}
			}
			if(viewNum != null){
				webPageDetail.setViewNum(viewNum);
			}
			//评论数
			Integer commentNum = null;
			if(!page.select("#cmtNum, #linkComment").isEmpty()){
				if(!StringUtils.isBlank(page.select("#cmtNum, #linkComment").first().text())) {
					commentNum = Integer.parseInt(page.select("#cmtNum, #linkComment").first().text());
				}
			}
			if(!page.select(".article-pl").isEmpty()){
				String tmp = page.select(".article-pl").first().text().replace("评论", "");
				if(StringUtils.isBlank(tmp)) {
					commentNum = Integer.parseInt(tmp);
				}
			}
			if(commentNum != null){
				webPageDetail.setViewNum(commentNum);
			}
			//爬取时间
			webPageDetail.setCrawlTime(new Date());
			webPageDetailMapper.insertSelective(webPageDetail);
			HduStarter.baiduSearchSize.getAndIncrement();
		}
	}

	/**
	 * 解析网页的视频、图片等资源
	 * @param page
	 * @param next
	 */
	private void parseWebSource(Page page, CrawlDatums next){
		List<UrlRelation> urlRelationLs = new LinkedList<>();
		String srcUrl = page.getResponse().getRealUrl().toString();
		String domain = page.getResponse().getRealUrl().getHost();
		String path = page.getResponse().getRealUrl().getPath();

		//解析图片地址
		Elements imgs = page.select("img");
		if(!imgs.isEmpty()) {
			for (Element img : imgs) {
				//String src = img.attr("abs:src"); 获取绝对路径，暂时不用，可能存在bug
				String src = img.attr("src");
				if(src==null || src.equals("") || src.contains("{")){ //js动态加载、vue的图片暂时取不到
					continue;
				}
				if(src.startsWith("http") || src.startsWith("//")){ //绝对路径

				}else if(src.startsWith("/")){ //以域名为基的相对路径
					src = domain+src;
				}else if(src.startsWith("./")){ //当前目录下
					src = srcUrl + src.substring(1);
				}else if(src.startsWith("../")){ //上一目录下
					src = srcUrl.substring(0, srcUrl.lastIndexOf("/")) + src.substring(2);
				}
				else { //图片文件目录和当前文件在同一目录下的相对路径
					src = domain + path.substring(0, path.lastIndexOf("/")+1) + src;
					//System.out.println("src：" + src);
				}
				if(src.contains("http://www.52ai.com/zb_users/upload/2018/03/201") || src.contains("http://j5.dfcfw.com/image/2016")){
					System.out.println(srcUrl);
					System.out.println(src);
				}
				UrlRelation urlRelation = new UrlRelation(srcUrl, src, (short)2, new Date());
				urlRelationLs.add(urlRelation);
			}
		}

		//解析视频地址
		String videoUrl = null;
		if(page.matchUrl("https://.*\\.ku6\\.com.*")) {//酷6视频地址
			if (page.getHtml().contains("flvURL")) {
				String tmp = page.getHtml().substring(page.getHtml().indexOf("flvURL"), page.getHtml().indexOf("flvURL") + 100);
				videoUrl = tmp.substring(tmp.indexOf("http"), tmp.indexOf("\","));
			}
			if (page.getHtml().contains("playUrl")) {
				String tmp = page.getHtml().substring(page.getHtml().indexOf("playUrl"), page.getHtml().indexOf("playUrl") + 100);
				videoUrl = tmp.substring(tmp.indexOf("http"), tmp.indexOf("\","));
			}
			if (page.select("video").first() != null) {
				Element source = page.select("video source").first();
				if (source == null) {
					return;
				}
				videoUrl = source.attr("src");
			}
		}else if(page.matchUrl("http://baishi\\.baidu\\.com/watch/.*")) { //百度视频
			int start = page.getHtml().indexOf("video=http");
			if (start == -1) {
				logger.info("fail:" + page.getUrl());
				return;
			}
			String tmp = page.getHtml().substring(start + 6, start + 500);
			int end = tmp.indexOf("';");
			String downloadUrl = tmp.substring(0, end);
			try {
				videoUrl = URLDecoder.decode(downloadUrl, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}else if (page.matchUrl("http://video\\.eastday\\.com.*")) { //东方头条视频
			int start = page.getHtml().indexOf("mp4 = ");
			if (start == -1) {
				logger.info("fail:" + page.getUrl());
				return;
			}
			String tmp = page.getHtml().substring(start + 7, start + 100);
			int end = tmp.indexOf("mp4\";");
			videoUrl = "http:" + tmp.substring(0, end + 3);
		}else if (page.matchUrl("http://xiyou\\.cctv\\.com/v-.*")) {//cctv视频播放页面，不是视频地址
			String videoId = page.getUrl().substring(page.getUrl().indexOf("v-") + 2, page.getUrl().indexOf(".html"));
			//爬取cctv视频地址信息接口
			next.add(datumGenerator.generateCCTVVideo(videoId, page.getUrl()));
		}
		if(videoUrl != null){
			UrlRelation urlRelation = new UrlRelation(srcUrl, videoUrl, (short)1, new Date());
			urlRelationLs.add(urlRelation);
		}

		//插入数据库
		if(!urlRelationLs.isEmpty()) {
			urlRelationMapper.batchInsert(urlRelationLs);
		}
	}

	/**
	 * 解析网页下面的超链接
	 * @param page
	 * @param next
	 */
	private void paerseHref(Page page, CrawlDatums next){
		Elements as = page.select("a");
		for (Element a : as) {
			if (a.hasAttr("href")) {
				String href = a.attr("abs:href");//获取超链接
				String title = a.text();
				if(href.startsWith("http") && title.contains(page.meta("keyword"))) { //过滤与关键字无关的超链接
					next.add(datumGenerator.generateBaiduSearchRs(href, page.meta("keyword"), page.getResponse().getRealUrl().toString()));
				}
			}
		}
	}
}
