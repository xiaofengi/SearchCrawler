package org.crawler.processor.youtube;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import org.crawler.constants.DatumConstants;
import org.crawler.main.HduStarter;
import org.crawler.processor.Processor;
import org.crawler.util.DownloadUtil;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class YoutubePlayProcessor implements Processor{

    @Override
    public void process(Page page, CrawlDatums next) {
        Matcher matcher = Pattern.compile("(?<=url_encoded_fmt_stream_map\":\").*(?=\",)").matcher(page.getHtml());
        if(matcher.find()){
            String[] videoUrlInfos = matcher.group().split("type=video%2F");
            Pattern pattern = Pattern.compile("(?<=url=).*(,)?");
            for(int i=1; i<videoUrlInfos.length; i++){
                String videoUrlInfo = videoUrlInfos[i];
                String fileFormat = videoUrlInfo.substring(0, videoUrlInfo.indexOf("%3B"));
                matcher = pattern.matcher(videoUrlInfo);
                if(matcher.find()) {
                    String downloadUrl = null;
                    try {
                        downloadUrl = URLDecoder.decode(matcher.group(), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String fileName = DatumConstants.DOWNLOAD_LOC_YOUTUBE + page.meta("videoId") + "." + fileFormat;
                    boolean success = DownloadUtil.download(downloadUrl, fileName, 3600, page.meta("referer"));
                    System.out.println("下载");
                    System.out.println(downloadUrl);
                    if(success) {
                        System.out.println("成功");
                    }else {
                        System.out.println("失败");
                    }
                    HduStarter.youtubeSize.getAndIncrement();
                }
            }
        }
    }
}
