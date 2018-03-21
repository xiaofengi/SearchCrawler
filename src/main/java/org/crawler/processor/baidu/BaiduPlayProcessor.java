package org.crawler.processor.baidu;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.crawler.processor.Processor;
import org.crawler.util.DownloadUtil;
import org.springframework.stereotype.Component;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;

@Component
public class BaiduPlayProcessor implements Processor{

	@Override
	public void process(Page page, CrawlDatums next) {
		int start = page.getHtml().indexOf("video=http");
		if(start == -1) {
			return;
		}
		String tmp = page.getHtml().substring(start+6, start+500);
		int end = tmp.indexOf("';");
		String downloadUrl = tmp.substring(0, end);
		try {
			downloadUrl = URLDecoder.decode(downloadUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String fileName = "E:\\Downloads\\crawler\\" + downloadUrl.substring(downloadUrl.indexOf("com/")+4, downloadUrl.indexOf("?"));
		boolean success = DownloadUtil.download(downloadUrl,  fileName, 3600);
		System.out.print("下载(" + downloadUrl );
		if(success) {
			System.out.println(")成功");
		}else {
			System.out.println(")失败");
		}
	}

}
