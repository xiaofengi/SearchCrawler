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
	public static final String DOWNLOAD_LOC_YOUTUBE = "E:\\Downloads\\crawler\\youtube\\";

	public static final String BAIDU_SEARCH_URL = "https://www.baidu.com/s?rn=50&ie=utf-8&wd=%s&pn=%d";
	public static final String BAIDU_VIDEO_SEARCH_URL = "http://v.baidu.com/v?rn=50&ie=utf-8&word=%s&pn=%d&sc=%d";//rn:每页数量  pn:页面数 sc:来源 du:时长 pd:更新时间
	public static final String CCTV_VIDEO_INTERFACE = "http://xiyou.cctv.com/interface/index?videoId=%s";
	public static final String YOUTUBE_SEARCH_URL = "https://www.youtube.com/results?search_query=%s&pbj=1";
    public static final String YOUTUBE_PLAY_URL = "https://www.youtube.com/watch?v=%s";
    public static final String TENCENT_VIDEO_INFO = "https://h5vv.video.qq.com/getinfo?vid=%s&otype=%s&guid=%s&platform=%s&sdtfrom=%s&_qv_rmt=%s&_qv_rmt2=%s";
    public static final String FACEBOOK_PEOPLE_SEARCH__PC = "https://www.facebook.com/search/people/?q=%s";  //用户tab下
	public static final String FACEBOOK_PEOPLE_SEARCH__MOBILE = "https://m.facebook.com/search/people/?q=%s&pn=%d"; //用户tab下
    public static final String FACEBOOK_FRIENDS_LIST_PC = "https://www.facebook.com/ajax/pagelet/generic.php/AllFriendsAppCollectionPagelet?dpr=2&data=%7B%22collection_token%22%3A%22100021470634366%3A2356318349%3A2%22%2C%22cursor%22%3A%22MDpub3Rfc3RydWN0dXJlZDoxMDAwMjQ1NTM0ODk0OTk%3D%22%2C%22disablepager%22%3Afalse%2C%22overview%22%3Afalse%2C%22profile_id%22%3A%22100021470634366%22%2C%22pagelet_token%22%3A%22AWvq3tLYN1vhLtrjycbFevs2bZNtqHu4_HeKRL8XhiVrNw6K84__Gi-ZH5IZhx0BUdE%22%2C%22tab_key%22%3A%22friends%22%2C%22lst%22%3A%22100025261115546%3A100021470634366%3A1523202510%22%2C%22ftid%22%3Anull%2C%22order%22%3Anull%2C%22sk%22%3A%22friends%22%2C%22importer_state%22%3Anull%7D&__user=100025261115546&__a=1&__dyn=7AgNe-4am2d2u6aJGeFxqewKKEKEW8x2C-C267UqwWhE98nwgUy1wx-K9wPGi2uUG4XzEeUK3uczobohwkuu58O5U7SidwBx62q3O69LwZx-0IpU2BxCqUpwMxK1Iwgovy88E6WdzEjG48y1IyaQ6e4oC2by8vBo4-4EC16Vk1ey84a&__req=n&__be=1&__pc=PHASED%3ADEFAULT&__rev=3794589&__spin_r=3794589&__spin_b=trunk&__spin_t=1523202700";
    public static final String FACEBOOK_FRIENDS_LIST_MOBILE = "https://m.facebook.com/thomas.okoto.9/friends?unit_cursor=%s&lst=%s";
}
