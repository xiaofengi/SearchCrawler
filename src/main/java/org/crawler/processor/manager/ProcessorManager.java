package org.crawler.processor.manager;

import java.util.HashMap;
import java.util.Map;

import org.crawler.constants.ProcessorType;
import org.crawler.exceptions.NoPageProcessorException;
import org.crawler.processor.Processor;
import org.crawler.processor.baidu.BaiduPlayProcessor;
import org.crawler.processor.baidu.BaiduSearchProcessor;
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
		processors.put(ProcessorType.PROCESSOR_TYPE_BAIDU_SEARCH, applicationContext.getBean(BaiduSearchProcessor.class));
		processors.put(ProcessorType.PROCESSOR_TYPE_BAIDU_PLAY, applicationContext.getBean(BaiduPlayProcessor.class));
	}
}
