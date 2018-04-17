package org.crawler.processor.baidu;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import org.crawler.processor.Processor;
import org.springframework.stereotype.Component;

@Component
public class BaiduSearchRsProcessor implements Processor{

    @Override
    public void process(Page page, CrawlDatums next) {
        System.out.println(page.getResponse().getRealUrl());
    }
}
