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
public class CoderbusyProcessor implements Processor{

    private static final Logger logger = LoggerFactory.getLogger(CoderbusyProcessor.class);

    @Resource
    private DatumGenerator datumGenerator;
    @Resource
    private ProxyEntityMapper proxyEntityMapper;

    @Override
    public void process(Page page, CrawlDatums next) {
        String country = page.meta("country");
        int pageIndex = Integer.parseInt(page.meta("pageIndex"));
        Elements trs = page.select("tbody tr");
        List<ProxyEntity> proxyEntityList = new ArrayList<>();
        if(!trs.isEmpty()){
            for (Element tr : trs){
                Elements tds = tr.getElementsByTag("td");
                String host = tds.get(0).text();
                int port = Integer.parseInt(tds.get(2).text().trim());
                String location = tds.get(3).text();
                String type = tds.get(5).text();
                String anonymousType = tds.get(7).text();
                String resTime = tds.get(10).text();
                ProxyEntity proxyEntity = new ProxyEntity(host, port, location, type, anonymousType, resTime, false, true, new Date());
                proxyEntityList.add(proxyEntity);
            }
            //插入数据库
            if(!proxyEntityList.isEmpty()) {
                proxyEntityMapper.batchInsert(proxyEntityList);
                logger.info("插入" + country + "的代理ip共" + proxyEntityList.size() + "条");
            }
        }
        if(pageIndex == 1){
            int resultNum = 0;
            Elements numElement = page.select(".pagination li:last-child");
            if(!numElement.isEmpty()){
                String totalNumText = numElement.first().text();
                int totalNum = Integer.parseInt(totalNumText.replace("共","").replace("条","").trim());
                resultNum = totalNum;
                if(totalNum > 50) {
                    for (int i = 2; totalNum>50; i++) {
                        totalNum -= 50;
                        next.add(datumGenerator.generateCoderbusy(country, i));
                    }
                }
            }else {
                resultNum = proxyEntityList.size();
            }
            logger.info("搜索到" + country + "的代理ip共" + resultNum + "条");
        }
    }

}
