package org.crawler.crawler;

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
import org.crawler.mysql.mapper.KeywordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.base.Splitter;

import cn.edu.hfut.dmic.webcollector.crawler.Crawler;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;

@Component
public class SeedGenerator implements CrawlerBeginListener{
	@Autowired
	private KeywordMapper keywordMapper;
	
	@Value("${crawler.seed.type}")
	private String crawlerType;
	@Value("${crawler.seed.goodsIds}")
	private String goodsIds;
	@Value("${crawler.hqt.token:12574478}")
	private String hqtToken;
	@Value("${crawler.machine}")
	private Integer machine;
	
	@Value("${crawler.beginType}")
	private String beginType;
	
	public static Map<Long,Long> rootCatMap;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmm");

	@Autowired
	private DatumGenerator datumGenerator;
	
	public void addSeed(Crawler crawler) {
		switch (crawlerType) {
		case CrawlerType.KEYWORD:
			generateKeyword(crawler);
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
			case CrawlerType.FACEBOOK_FRIENDS_LIST:
			generateFbFriendsList(crawler);
			break;
		default:
			break;
		}
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

	private void generateKeyword(Crawler crawler) {
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
