package org.crawler.exceptions;

public class PipelineExcption extends CrawlerException{

	private static final long serialVersionUID = 1L;

	public PipelineExcption() {
		super();
	}

	public PipelineExcption(String message) {
		super(message);
	}
}
