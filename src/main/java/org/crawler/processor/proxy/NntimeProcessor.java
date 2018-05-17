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
import java.util.*;

@Component
public class NntimeProcessor implements Processor{

    private static final Logger logger = LoggerFactory.getLogger(NntimeProcessor.class);

    @Resource
    private DatumGenerator datumGenerator;
    @Resource
    private ProxyEntityMapper proxyEntityMapper;

    @Override
    public void process(Page page, CrawlDatums next) {
        int pageIndex = Integer.parseInt(page.meta("pageIndex"));

        Elements trs = page.select(".odd, .even");
        if(trs.isEmpty()){
           return;
        }

        //数字字母对应关系
        String mapperText = page.select("head [type=text/javascript]").get(1).data().replace("\n", "");
        String[] mappers = mapperText.split(";");
        Map<String, String> map = new HashMap<>();
        for(String mapper : mappers){
            if(!mapper.equals("")) {
                map.put(mapper.split("=")[0], mapper.split("=")[1]);
            }
        }

        List<ProxyEntity> proxyEntityList = new ArrayList<>();
        for(Element tr : trs){
            Elements tds = tr.getElementsByTag("td");
            //更新时间
            String Updated = tds.get(3).text();
            if(!Updated.contains("2018")){ //只爬取今年的数据
                return;
            }
            //ip和端口
            String host = tds.get(1).text();
            if(host.contains(":$")){
                return;
            }
            String script = tds.get(1).getElementsByTag("script").first().data();
            host = host.replace(script, "").trim();
            String tmp = script.substring("document.write(\":\"".length(), script.length()-1).replace("+", "");
            StringBuilder portNum = new StringBuilder();
            for(int i=0; i<tmp.length(); i++){
                portNum.append(map.get(tmp.charAt(i)+""));
            }
            int port = Integer.parseInt(portNum.toString());
            //匿名类型
            String anonymousType = tds.get(2).text();
            //国家
            String location = tds.get(4).text();
            ProxyEntity proxyEntity = new ProxyEntity(host, port, location, null, anonymousType, null, false, true, new Date());
            proxyEntityList.add(proxyEntity);
        }
        //插入数据库
        if(!proxyEntityList.isEmpty()) {
            proxyEntityMapper.batchInsert(proxyEntityList);
            logger.info("插入nntime的代理ip共" + proxyEntityList.size() + "条");
        }

        //爬取下一页
        next.add(datumGenerator.generateNntime(++pageIndex));
    }
}
