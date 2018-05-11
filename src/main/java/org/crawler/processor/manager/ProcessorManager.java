package org.crawler.processor.manager;

import java.util.HashMap;
import java.util.Map;

import org.crawler.constants.ProcessorType;
import org.crawler.exceptions.NoPageProcessorException;
import org.crawler.processor.Processor;
import org.crawler.processor.baidu.BaiduSearchRsProcessor;
import org.crawler.processor.baidu.video.PlayPageProcessor;
import org.crawler.processor.baidu.BaiduSearchProcessor;
import org.crawler.processor.baidu.video.BaiduVideoSearchProcessor;
import org.crawler.processor.baidu.video.Ku6PlayProcessor;
import org.crawler.processor.facebook.FbFriendsListProcessor;
import org.crawler.processor.facebook.FbSearchProcessor;
import org.crawler.processor.proxy.CoderbusyProcessor;
import org.crawler.processor.youtube.YoutubeListProcessor;
import org.crawler.processor.youtube.YoutubePlayProcessor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;

@Component
public class ProcessorManager implements ApplicationContextAware{
	private Map<String, Processor> processors = new HashMap<String, Processor>();
	
	public void process(Page page, CrawlDatums next) {
		Processor pageProcess = getProcessor(page);
		pageProcess.process(page, next);
	}

	public Processor getProcessor(Page page) {
		String processorType = page.meta(ProcessorType.PROCESSOR_TYPE);	
		if(processorType == null || !(processors.keySet().contains(processorType))) {
			throw new NoPageProcessorException();
		}	
		return processors.get(processorType);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		processors.put(ProcessorType.BAIDU_SEARCH, applicationContext.getBean(BaiduSearchProcessor.class));
		processors.put(ProcessorType.BAIDU_SEARCH_RS, applicationContext.getBean(BaiduSearchRsProcessor.class));
		processors.put(ProcessorType.BAIDU_VIDEO_SEARCH, applicationContext.getBean(BaiduVideoSearchProcessor.class));
		processors.put(ProcessorType.PLAY_PAGE, applicationContext.getBean(PlayPageProcessor.class));
		processors.put(ProcessorType.KU6_PLAY, applicationContext.getBean(Ku6PlayProcessor.class));
		processors.put(ProcessorType.YOUTUBE_LIST, applicationContext.getBean(YoutubeListProcessor.class));
		processors.put(ProcessorType.YOUTUBE_PLAY, applicationContext.getBean(YoutubePlayProcessor.class));
		processors.put(ProcessorType.FACEBOOK_SEARCH, applicationContext.getBean(FbSearchProcessor.class));
		processors.put(ProcessorType.FACEBOOK_FRIENDS_LIST, applicationContext.getBean(FbFriendsListProcessor.class));
		processors.put(ProcessorType.CODDERBUSY, applicationContext.getBean(CoderbusyProcessor.class));
	}
}
