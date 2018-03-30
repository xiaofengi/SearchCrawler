package org.crawler.processor.youtube;

import java.util.List;
import java.util.Map;

import org.crawler.crawler.DatumGenerator;
import org.crawler.processor.Processor;
import org.springframework.stereotype.Component;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;

import javax.annotation.Resource;

@Component
public class YoutubeListProcessor implements Processor{

	@Resource
	private DatumGenerator datumGenerator;

	@Override
	public void process(Page page, CrawlDatums next) {
		List<Map> responseList = new Gson().fromJson(page.getHtml(), new TypeToken<List<Map>>() {}.getType());
		int count = 0;
		for(Map map:responseList){
			if(map.containsKey("response")){
				Map response = (Map)map.get("response");
				Map contents = (Map)response.get("contents");
				Map twoColumnSearchResultsRenderer = (Map)contents.get("twoColumnSearchResultsRenderer");
				Map primaryContents = (Map)twoColumnSearchResultsRenderer.get("primaryContents");
				Map sectionListRenderer = (Map)primaryContents.get("sectionListRenderer");
				List<Map> contents1 = (List<Map>)sectionListRenderer.get("contents");
				for(Map content:contents1) {
					Map itemSectionRenderer = (Map) content.get("itemSectionRenderer");
					List<Map> videoContents = (List<Map>)itemSectionRenderer.get("contents");
					for(Map videoContent:videoContents){
						Map videoRenderer = (Map)videoContent.get("videoRenderer");
						String videoId = (String)videoRenderer.get("videoId");
						next.add(datumGenerator.generateYoutubePlay(videoId));
						count++;
					}
				}
			}
		}
		System.out.println("共搜索到" + count + "条视频");
	}

}
