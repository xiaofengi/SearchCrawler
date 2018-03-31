package org.crawler.constants;

public class DatumConstants {	
	public static final int SC_ALL = 0;
	public static final int SC_IQIYI = 1;
	public static final int SC_PPS = 2;
	public static final int SC_SOHU = 3;
	public static final int SC_TENCENT = 4;
	public static final int SC_LE = 5;
	public static final int SC_IFENG = 6;
	public static final int SC_KANKAN = 7;
	public static final int SC_V1 = 8;
	public static final int SC_CCTV = 9;
	public static final int SC_1905 = 10;
	public static final int SC_BAOMIHUA = 11;
	public static final int SC_56 = 12;
	public static final int SC_PPTV = 14;
	public static final int SC_WASU = 15;
	public static final int SC_YOUKU = 16;
	public static final int SC_TUDOU = 17;
	public static final int SC_BAOFENG = 18;
	public static final int SC_FUN = 20;
	public static final int SC_MGTV = 21;
	public static final int SC_CZTV = 22;
	public static final int SC_AIPAI = 23;
	public static final int SC_YINYUETAI = 24;
	public static final int SC_EASTDAY = 25;
	
	public static final String DOWNLOAD_LOC_BAIDU = "E:\\Downloads\\crawler\\baidu\\baidu\\";
	public static final String DOWNLOAD_LOC_EASTDAY = "E:\\Downloads\\crawler\\baidu\\eastday\\";
	public static final String DOWNLOAD_LOC_CCTV = "E:\\Downloads\\crawler\\baidu\\cctv\\";
	public static final String DOWNLOAD_LOC_KU6 = "E:\\Downloads\\crawler\\baidu\\ku6\\";
	public static String DOWNLOAD_LOC_YOUTUBE = "E:\\Downloads\\crawler\\youtube\\";

	public static final String BAIDU_SEARCH_URL = "http://v.baidu.com/v?rn=20&ie=utf-8&word=%s&pn=%d&sc=%d";//rn:每页数量  pn:页面数 sc:来源 du:时长 pd：更新时间
	public static final String CCTV_VIDEO_INTERFACE = "http://xiyou.cctv.com/interface/index?videoId=%s";
	public static final String YOUTUBE_SEARCH_URL = "https://www.youtube.com/results?search_query=%s&pbj=1";
    public static final String YOUTUBE_PLAY_URL = "https://www.youtube.com/watch?v=%s";
}
