package org.crawler.processor.proxy;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import org.crawler.crawler.DatumGenerator;
import org.crawler.mysql.mapper.ProxyEntityMapper;
import org.crawler.mysql.model.ProxyEntity;
import org.crawler.processor.Processor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class Data5uProcessor implements Processor{

    private static final Logger logger = LoggerFactory.getLogger(Data5uProcessor.class);

    @Resource
    private DatumGenerator datumGenerator;
    @Resource
    private ProxyEntityMapper proxyEntityMapper;

    @Override
    public void process(Page page, CrawlDatums next) {
        String proxyType = page.meta("proxyType");
        int times = Integer.parseInt(page.meta("times"));

        Elements uls = page.select(".l2");
        logger.info("搜索到无忧" + proxyType + "代理共" + uls.size() + "条");
        if(!uls.isEmpty()) {
            List<ProxyEntity> proxyEntityList = new ArrayList<>();
            for (Element ul : uls) {
                Elements spans = ul.getElementsByTag("span");
                String host = spans.get(0).text();
                int port = Integer.parseInt(spans.get(1).text().trim());
                String anonymousType = spans.get(2).text();
                String type = spans.get(3).text();
                String location = spans.get(4).text() + " " + spans.get(5).text();
                location = location.replace("X", "").trim();
                String resTime = spans.get(7).text();
                ProxyEntity proxyEntity = new ProxyEntity(host, port, location, type, anonymousType, resTime, false, true, new Date());
                proxyEntityList.add(proxyEntity);
            }
            //插入数据库
            if(!proxyEntityList.isEmpty()) {
                proxyEntityMapper.batchInsert(proxyEntityList);
                logger.info("插入无忧" + proxyType + "代理共" + proxyEntityList.size() + "条");
            }
        }

        //循环爬取下一页
        if(times < 5) {
            next.add(datumGenerator.generateData5u(page.getUrl(), proxyType, ++times));
        }
    }

}
