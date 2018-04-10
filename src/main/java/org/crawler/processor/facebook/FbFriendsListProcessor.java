package org.crawler.processor.facebook;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import com.google.gson.Gson;
import org.crawler.processor.Processor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class FbFriendsListProcessor implements Processor{

    @Override
    public void process(Page page, CrawlDatums next) {
        System.out.println("返回数据：" + page.getHtml());
        String json = page.getHtml().substring(page.getHtml().indexOf("{"), page.getHtml().lastIndexOf("}")+1);
        Map res = new Gson().fromJson(json, Map.class);
        Map payload = (Map)res.get("payload");
        List actions = (List)payload.get("actions");
        //friends list html
        String html = ((Map)actions.get(0)).get("html").toString();
        Document document = Jsoup.parse(html);
        Elements h1s = document.select("h1");
        int friendsNum = 0;
        for(Element h1:h1s){
            Element aTag = h1.getElementsByTag("a").first();
            System.out.println("好友名：" + aTag.text());
            System.out.println("好友链接：" + "https://m.facebook.com" + aTag.attr("href"));
            friendsNum++;
        }
        System.out.println("好友数：" + friendsNum);
    }
}
