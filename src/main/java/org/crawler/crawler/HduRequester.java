package org.crawler.crawler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.crawler.constants.ProcessorType;
import org.crawler.listener.CrawlerBeginListener;
import org.crawler.listener.CrawlerEndListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.net.HttpRequest;
import cn.edu.hfut.dmic.webcollector.net.HttpResponse;
import cn.edu.hfut.dmic.webcollector.net.Requester;

@Component
public class HduRequester implements Requester, CrawlerBeginListener, CrawlerEndListener{
	private static final Logger logger = LoggerFactory.getLogger(HduRequester.class);
	private static final ExecutorService exec = Executors.newFixedThreadPool(1);
	private String detailCookie;
	private boolean flag = true;
	
	@Value("${crawler.machine}")
	private Integer machine;
	@Value("${crawler.proxy.enable}")
	private boolean proxyEnable;
	@Value("${crawler.requestDetail}")
	private boolean requestDetail;
	
	@Override
	public HttpResponse getResponse(CrawlDatum crawlDatum) throws Exception {
		HttpResponse res = null;
		HttpRequest request = null;
		try{
			request = new HttpRequest(crawlDatum);
			request.setMAX_REDIRECT(4);
			setHeader(crawlDatum, request);
			if(proxyEnable){
			}
			res = request.getResponse();
		}  catch (Exception e) {
			throw e;
		} finally {
		}
		return res;
	}

	private void setHeader(CrawlDatum crawlDatum, HttpRequest request) {
		request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
		switch (crawlDatum.meta(ProcessorType.PROCESSOR_TYPE)) {
			case ProcessorType.PROCESSOR_TYPE_BAIDU_SEARCH:
				//request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
				request.setHeader("Host", "v.baidu.com");
				request.setHeader("Referer", "http://v.baidu.com/v");
				request.setCookie("BIDUPSID=165A3B8D1F93219D14C0B4A8138AEF6A; PSTM=1503732400; __cfduid=db0db3f162e9aee15f3eb36abaa675d1f1508568007; BAIDUID=17956543C27BB1FA4E9D7A458BC3F00C:FG=1; MCITY=-%3A; d_ad_beforeplay_today_num=6; H_PS_PSSID=1460_19033_21122; BDORZ=FFFB88E999055A3F8A630C64834BD6D0; BDRCVFR[X7WRLt7HYof]=mk3SLVN4HKm; PSINO=5; bdv_right_ad_poster=1; BUBBLESDEl=1");
				break;
			case ProcessorType.PROCESSOR_TYPE_BAIDU_PLAY:
				//request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
				if(crawlDatum.meta("referer") != null) {
					request.setHeader("referer", crawlDatum.meta("referer"));
				}
				break;
			case ProcessorType.PROCESSOR_TYPE_YOUTUBE_LIST:
				//request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
				request.setCookie("VISITOR_INFO1_LIVE=EzJV7E3gjr0; SID=NAVMifB-wvRZg_XV7HICBT67GT1tKvBQUCq2xskgMTVQhC6LKJaR3apM_949e0lBDSBPtw.; HSID=A_6HcDLRBttV-fEHi; SSID=AteEO8zoelTmY0_4b; APISID=qMYD0zY0RxQ3EqPO/AxuY5BI-_YHQrrbIj; SAPISID=y35kNgi8LP2Nt-yZ/AUY00eUQzGh8jj00y; CONSENT=YES+CN.zh-CN+20170903-09-0; LOGIN_INFO=ACn9GHowRAIgBQH-LEMv1Nuq0klFnwOnmBCHn37kMJb9FnVHiNunlC4CIGVb_ifU0HQK_oJXMSS75NjmyTjp7cD-BB_IAA1E5fMJ:QUxJMndvR2lObFpQdEhQUktfY2hyZ25mOHN2LTZHS0JFWjQzWDNEdXl6dUpMSXFJX24tZDZyVUVrSmt1RnpUUXVERVF6bFZ1S09KOE1yQUkxMVNBVjc0NXN6N1EyTmtDTmVaZi1zUEpyUV9DcDEzQTFySEEta1V2TE5xZTl3dUpsT1d0RVRac1FtLUJ0UkZyVkVwZ3NOMnZZLVVZLXNDbFc0djZ2V3FFUklfVGJ4WHZrYk5pMXBn; PREF=f1=50000000&al=zh-CN; YSC=kaqiTayd01Q; ST-1i14npa=oq=%E4%BA%BA%E5%B7%A5%E6%99%BA%E8%83%BD&gs_l=youtube.3..0i12k1l10.51203.55934.0.56253.20.17.2.0.0.0.730.7059.3-11j1j2j2.16.0....0...1ac.4.64.youtube..5.15.6012...0.0.GAHewAixluE&feature=web-masthead-search&itct=CCoQ7VAiEwimh7_A4JHaAhUHx8EKHZmgDX8ojh4%3D&csn=ofm8WuayG4eOhwaZwbb4Bw");
				request.setHeader("referer", " https://www.youtube.com/");
				request.addHeader("x-client-data", "CIS2yQEIpbbJAQjEtskBCKmdygEIqKPKAQ==");
				request.addHeader("x-spf-previous", "https://www.youtube.com/");
				request.addHeader("x-spf-referer", "https://www.youtube.com/");
				request.addHeader("x-youtube-client-name", "1");
				request.addHeader("x-youtube-client-version", "2.20180328");
				request.addHeader("x-youtube-identity-token", "QUFFLUhqbWtiMHNfdC1UZE96eEFERFlFbUdaRUJfOW8yZ3w=");
				request.addHeader("x-youtube-page-cl", "190715957");
				request.addHeader("x-youtube-page-label", "youtube.ytfe.desktop_20180327_5_RC0");
				request.addHeader("x-youtube-sts", "17616");
				request.addHeader("x-youtube-variants-checksum", "49f92a48c02b74aaca23e1c7b1fd5f9c");
				break;
			case ProcessorType.PROCESSOR_TYPE_FACEBOOK_FRIENDS_LIST:
				//facebook账号cookie
				request.setCookie("datr=6B3CWoBQ0_uvkPBIWSDMiO1l; sb=7R3CWhsjE_MJSIs2bPwriFb6; c_user=100025261115546; xs=35%3AIIrVA_3JwGiPFA%3A2%3A1522671085%3A-1%3A-1; fr=0wiotCZ6lTlDopX0T.AWW9qs8IncTTc5vWSt3nWOHcnoQ.Bawh3o.xf.AAA.0.0.Bawh3t.AWWEOFBl; pl=n; act=1522671572435%2F1; wd=1920x302; presence=EDvF3EtimeF1522671954EuserFA21B25261115546A2EstateFDutF1522671954526CEchFDp_5f1B25261115546F4CC");
		default:
			break;
		}	
	}

	@Override
	public void crawlerBegin() {
		if(requestDetail) {
			exec.execute(new Runnable() {
				@Override
				public void run() {
				}
			});
			exec.shutdown();
		}
	}


	@Override
	public void crawlerEnd() {
		this.flag = false;
	}
	
}
