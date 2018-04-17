package org.crawler.crawler;

import org.crawler.constants.DatumConstants;
import org.crawler.constants.ProcessorType;
import org.crawler.entity.FbFriendsListParam;
import org.springframework.stereotype.Component;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;

@Component
public class DatumGenerator {
	//private static final Logger logger = LoggerFactory.getLogger(DatumGenerator.class);
	
	/**
	 * 生成百度搜索列表
	 * @param keyword 搜索关键字
	 * @param pn 页面数(0,10,20,30...)
	 * @return
	 */
	public CrawlDatum generateBaiduSearchList(String keyword, int pn) {
		return new CrawlDatum(String.format(DatumConstants.BAIDU_SEARCH_URL, keyword, pn))
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_BAIDU_SEARCH)
				.meta(keyword, keyword)
				.meta("pn", String.valueOf(pn));
	}

	/**
	 * 百度搜索结果链接
	 * @param href 链接
	 * @return
	 */
	public CrawlDatum generateBaiduSearchRs(String href) {
		return new CrawlDatum(href)
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_BAIDU_SEARCH_RS);
	}
	
	/**
	 * 生成百度视频搜索列表
	 * @param keyword 搜索关键字
	 * @param pn 页数标记(0,60,80,100...)
	 * @param sc 视频来源
	 * @return
	 */
	public CrawlDatum generateVideoList(String keyword, int pn, int sc) {
		return new CrawlDatum(String.format(DatumConstants.BAIDU_VIDEO_SEARCH_URL, keyword, pn, sc))
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_BAIDU_VIDEO_SEARCH)
				.meta("keyword", keyword)
				.meta("pn", String.valueOf(pn))
				.meta("sc", String.valueOf(sc));
	}

	/**
	 * 视频播放页面
	 * @param url 视频播放页面url，不是视频地址url
	 * @return
	 */
	public CrawlDatum generatePlayPage(String playPageUrl) {
		return new CrawlDatum(playPageUrl)
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_PLAY_PAGE);
	}

	/**
	 * 百度视频重定向页面
	 * @param redirectUrl 重定向中间url
	 * @param referer 引用页
	 * @return
	 */
	public CrawlDatum generateRedirectPlayPage(String redirectUrl, String referer) {
		return new CrawlDatum(redirectUrl)
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_PLAY_PAGE)
				.meta("referer", referer);
	}

	/**
	 * cctv视频地址接口
	 * @param videoId 视频id
	 * @param referer 引用页
	 * @return
	 */
	public CrawlDatum generateCCTVVideo(String videoId, String referer) {
		return new CrawlDatum(String.format(DatumConstants.CCTV_VIDEO_INTERFACE, videoId))
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_PLAY_PAGE)
				.meta("referer", referer)
				.meta("videoId", videoId);
	}

	/**
	 * 酷6视频播放页面
	 * @param playPageUrl 酷6视频播放地址
	 * @return
	 */
	public CrawlDatum generateKu6PlayPage(String playPageUrl) {
		return new CrawlDatum(playPageUrl)
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_KU6_PLAY);
	}

	/**
	 * youtube视频列表
	 * @param keyword 搜索关键字
	 * @return
	 */
	public CrawlDatum generateYoutubeList(String keyword) {
		try {
			keyword = URLEncoder.encode(keyword, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new CrawlDatum(String.format(DatumConstants.YOUTUBE_SEARCH_URL, keyword))
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_YOUTUBE_LIST);
	}

	/**
	 * youtube播放页面
	 * @param videoId 视频id
	 * @param referer 引用页
	 * @return
	 */
    public CrawlDatum generateYoutubePlay(String videoId, String referer) {
		return new CrawlDatum(String.format(DatumConstants.YOUTUBE_PLAY_URL, videoId))
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_YOUTUBE_PLAY)
				.meta("referer", referer)
				.meta("videoId", videoId);
    }

    /**
     * facebook好友列表
     * @param url 
     * @param fbFriendsListParam
     * @return
     */
    public CrawlDatum generateFbFriendsList(String url, FbFriendsListParam fbFriendsListParam) {
		StringBuilder param = new StringBuilder();
		boolean first=true;
		for (Field field : fbFriendsListParam.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			try {
				if (!Modifier.isStatic(field.getModifiers()) && field.get(fbFriendsListParam) != null) {
					if(!first) {
						param.append("&");
					}else {
						first = false;
					}
					param.append(field.getName()+"="+field.get(fbFriendsListParam));
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return new CrawlDatum(url)
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_FACEBOOK_FRIENDS_LIST)
				.meta("fbFriendLsParam", param.toString());
    }

    /**
     * facebook用户搜索列表
     * @param url
     * @return
     */
	public CrawlDatum generateFbSearch(String url) {
		return new CrawlDatum(url)
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_FACEBOOK_SEARCH);
	}

}