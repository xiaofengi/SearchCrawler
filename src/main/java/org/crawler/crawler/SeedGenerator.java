package org.crawler.crawler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

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
			case CrawlerType.PROXY_CODERBUSY:
				generateCoderbusy(crawler);
				break;
			case CrawlerType.PROXY_DATA5U:
				generateData5u(crawler);
				break;
			case CrawlerType.PROXY_NNTIME:
				generateNntime(crawler);
				break;
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

	private void generateNntime(Crawler crawler) {
		crawler.addSeed(datumGenerator.generateNntime(1));
	}

	private void generateData5u(Crawler crawler) {
		crawler.addSeed(datumGenerator.generateData5u("http://www.data5u.com/free/gwgn/index.shtml", "国外高匿", 1));
		crawler.addSeed(datumGenerator.generateData5u("http://www.data5u.com/free/gwpt/index.shtml", "国外普通", 1));
	}

	private void generateCoderbusy(Crawler crawler) {
		List<String> countryList = new ArrayList<>();
		//巴西
		countryList.add("br");
		//美国
		countryList.add("us");
		//印度尼西亚
		countryList.add("id");
		//俄罗斯
		countryList.add("ru");
		//法国
		countryList.add("fr");
		//印度
		countryList.add("in");
		//香港
		countryList.add("hk");
		//泰国
		countryList.add("th");
		//孟加拉
		countryList.add("bd");
		//乌克兰
		countryList.add("ua");
		//土耳其
		countryList.add("tr");
		//意大利
		countryList.add("it");
		//哥伦比亚
		countryList.add("co");
		//德国
		countryList.add("de");
		//英国
		countryList.add("gb");
		//阿根廷
		countryList.add("ar");
		//波兰
		countryList.add("pl");
		//伊朗
		countryList.add("ir");
		//加拿大
		countryList.add("ca");
		//厄瓜多尔
		countryList.add("ec");
		//捷克共和国
		countryList.add("cs");
		//日本
		countryList.add("jp");
		//巴基斯坦
		countryList.add("pk");
		//南非
		countryList.add("za");
		//柬埔寨
		countryList.add("kh");
		//尼泊尔
		countryList.add("np");
		//越南
		countryList.add("vn");
		//罗马尼亚
		countryList.add("ro");
		//伊拉克
		countryList.add("iq");
		//菲律宾
		countryList.add("ph");
		//尼日利亚
		countryList.add("ng");
		//台湾
		countryList.add("tw");
		//格鲁吉亚
		countryList.add("ge");
		//巴勒斯坦
		countryList.add("bl");
		//澳大利亚
		countryList.add("au");
		for(String country : countryList){
			crawler.addSeed(datumGenerator.generateCoderbusy(country, 1));
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
	}

}
