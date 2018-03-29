package org.crawler.processor.youtube;

import java.util.List;
import java.util.Map;

import org.crawler.processor.Processor;
import org.springframework.stereotype.Component;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;

@Component
public class YoutubeListProcessor implements Processor{

	@Override
	public void process(Page page, CrawlDatums next) {
		List<Map> responseList = new Gson().fromJson(page.getHtml(), new TypeToken<List<Map>>() {}.getType());
	}

}
