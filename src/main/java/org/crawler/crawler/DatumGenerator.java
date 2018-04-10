package org.crawler.crawler;

import org.crawler.constants.DatumConstants;
import org.crawler.constants.ProcessorType;
import org.crawler.entity.FbFriendsListParam;
import org.springframework.stereotype.Component;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Component
public class DatumGenerator {
	//private static final Logger logger = LoggerFactory.getLogger(DatumGenerator.class);
	
	public CrawlDatum generateKeyword(String keyword) {
		return new CrawlDatum(String.format(DatumConstants.BAIDU_SEARCH_URL, keyword))
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_BAIDU_SEARCH)
				.meta("pn", "0");
	}
	
	/**
	 * 生成百度视频搜索列表
	 * @param keyword 搜索关键字
	 * @param pn 页数标记(0,60,80,100...)
	 * @param sc 视频来源
	 * @return
	 */
	public CrawlDatum generateVideoList(String keyword, int pn, int sc) {
		return new CrawlDatum(String.format(DatumConstants.BAIDU_SEARCH_URL, keyword, pn, sc))
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_BAIDU_SEARCH)
				.meta("keyword", keyword)
				.meta("pn", String.valueOf(pn))
				.meta("sc", String.valueOf(sc));
	}

	public CrawlDatum generatePlayPage(String url) {
		return new CrawlDatum(url)
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_BAIDU_PLAY);
	}

	public CrawlDatum generateRedirectPlayPage(String redirectUrl, String url) {
		return new CrawlDatum(redirectUrl)
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_BAIDU_PLAY)
				.meta("referer", url);
	}

	public CrawlDatum generateCCTVVideo(String videoId, String referer) {
		return new CrawlDatum(String.format(DatumConstants.CCTV_VIDEO_INTERFACE, videoId))
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_BAIDU_PLAY)
				.meta("referer", referer)
				.meta("videoId", videoId);
	}

	public CrawlDatum generateKu6PlayPage(String testUrl) {
		return new CrawlDatum(testUrl)
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_KU6_PLAY);
	}

	public CrawlDatum generateYoutubeList(String keyword) {
		return new CrawlDatum(String.format(DatumConstants.YOUTUBE_SEARCH_URL, keyword))
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_YOUTUBE_LIST);
	}

    public CrawlDatum generateYoutubePlay(String videoId, String referer) {
		return new CrawlDatum(String.format(DatumConstants.YOUTUBE_PLAY_URL, videoId))
				.meta(ProcessorType.PROCESSOR_TYPE, ProcessorType.PROCESSOR_TYPE_YOUTUBE_PLAY)
				.meta("referer", referer)
				.meta("videoId", videoId);
    }

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
}