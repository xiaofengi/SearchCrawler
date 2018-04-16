package org.crawler.crawler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.crawler.constants.CrawlerType;
import org.crawler.constants.DatumConstants;
import org.crawler.entity.FbFriendsListParam;
import org.crawler.listener.CrawlerBeginListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import cn.edu.hfut.dmic.webcollector.crawler.Crawler;

@Component
public class SeedGenerator implements CrawlerBeginListener{
	
	@Value("${crawler.seed.type}")
	private String crawlerType;

	@Autowired
	private DatumGenerator datumGenerator;
	
	public void addSeed(Crawler crawler) {
		switch (crawlerType) {
			case CrawlerType.BAIDU_SEARCH:
				generateBaiduSearch(crawler);
				break;
			case CrawlerType.BAIDU_VIDEO_SEARCH:
				generateBaiduVideoSearch(crawler);
				break;
			case CrawlerType.PLAY_PAGE:
				generatePlayPage(crawler);
				break;
			case CrawlerType.KU6_PLAY_PAGE:
				generateKu6PlayPage(crawler);
				break;
			case CrawlerType.YOUTUBE_SEARCH:
				generateYoutubeList(crawler);
				break;
			case CrawlerType.FACEBOOK_SEARCH:
				generateFbSearch(crawler);
				break;
			case CrawlerType.FACEBOOK_FRIENDS_LIST:
				generateFbFriendsList(crawler);
				break;
		default:
			break;
		}
	}

	/**
	 * 添加百度搜索列表种子
	 * @param crawler
	 */
	private void generateBaiduSearch(Crawler crawler) {	
		crawler.addSeed(datumGenerator.generateBaiduSearchList("人工智能", 0));
	}

	private void generateFbSearch(Crawler crawler) {
		String testUrl = null;
		/*try {
			testUrl = String.format(DatumConstants.FACEBOOK_PEOPLE_SEARCH__MOBILE, URLEncoder.encode("陈", "utf-8"), 3);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}*/
		testUrl = "https://m.facebook.com/search/people/?q=%E9%99%88&source=filter&isTrending=0&tsid=0.11954887113110768&cursor=AbrY6z7fbogAYJc-Cvyeqcaq-11jbVNbN6gRKyzAdT2RnfNUV2FR46pl9UHgnZ-AeJhQJpdfgEGbP69sGTbX-IwwIzF6AgkIUXkSZDjpO2x2yBeMvwNkLLepsNTO_UxeQ1pWF370H9hCfV6zBVV5taWWrTOwz-0FUhVqDRMQgfEme4pOf_3B_OWYCJz8bBmns2N7_UHMT-PeqraRWEE95jHNQuAqcarsJ-EjL-CmZZKEjzoodYtEWHBJ4W_umwrDXXaC_LmDgVI7orhsFSf8wcEkxPxMW2GvSXr1ruBjO2x7eo_ii2vr7XiL0qoTvDhhj6eEFUR3uuBoeDZIoIUhH-61cRnfroRmXU6DgH6D_HZowDiTwsgLzef0kGr6sBUFtLXIJ7eLVekJe7Z-ng8LKrJB&pn=9&usid=1f9aec9195805a100579f16bd9d1d23f";
		crawler.addSeed(datumGenerator.generateFbSearch(testUrl));
	}

	private void generateFbFriendsList(Crawler crawler) {
		//String testUrl = "https://www.facebook.com/Kobe.Thomas/friends?lst=100025261115546%3A602278835%3A1522671544&source_ref=pb_friends_tl";
		String testUrl = String.format(DatumConstants.FACEBOOK_FRIENDS_LIST_MOBILE,
				"AQHRwhNQ-x4RNqL47js2DzbcMvO3Zz0Uf5ZPATeLCIxvuaRVg-B754A7mEDMB27_AHusqPkKRUVBGQE-PvyxiPYVQA", "100025261115546:100021470634366:1523351362");
		FbFriendsListParam param = new FbFriendsListParam("", "AQGVAivEuRac:AQGjdw5KStpw",
				"1KQdAmm1gxu4U4ifDgy8xfKl3oS9xG6UO3m2i5UfXwqovzE6u7E5G0w8hwmU3Mx61YCwoo3XwIwk9EdEnw9u0XoswvoszEG2i3G1Qw", "kn", "AYlH8ydFRLABWzA-KSPd6VQ26dyi-kPqsWtDmsUiRkJyGy0pzf2uQprh0cfPZud2WTZQyOklwrOKpygeV9qtuiSqisEcRtMJkye7wZ2tYK_TgQ",
				100025261115546L);
		crawler.addSeed(datumGenerator.generateFbFriendsList(testUrl, param));
	}

	private void generateYoutubeList(Crawler crawler) {
		crawler.addSeed(datumGenerator.generateYoutubeList("人工智能"));
	}

	private void generateKu6PlayPage(Crawler crawler) {
		String testUrl = "https://www.ku6.com/video/detail?id=dFH1x8pAQPM1HGRH_1kPhcqqlGQ.";
		crawler.addSeed(datumGenerator.generateKu6PlayPage(testUrl));
	}

	private void generateBaiduVideoSearch(Crawler crawler) {
		crawler.addSeed(datumGenerator.generateVideoList("人工智能", 0, DatumConstants.SC_CCTV));
	}
	
	private void generatePlayPage(Crawler crawler) { //测试用
		String testUrl = "http://v.youku.com/v_show/id_XNTg1MzEzNzE2.html";
		crawler.addSeed(datumGenerator.generatePlayPage(testUrl));
	}

	@Override
	public void crawlerBegin() {
		// TODO Auto-generated method stub
		
	}

}
