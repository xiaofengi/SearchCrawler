package org.crawler.crawler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.crawler.constants.CrawlerType;
import org.crawler.constants.DatumConstants;
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
		default:
			break;
		}
	}

	private void generateKeyword(Crawler cralwer) {
		cralwer.addSeed(datumGenerator.generateVideoList("人工智能", 0, DatumConstants.SC_CCTV));
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
