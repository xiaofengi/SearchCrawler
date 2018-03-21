package org.crawler.monitor;

public class MonitorParam {
	
	private long crawlerCount;		//当前爬虫任务爬到的视频总数量
	
	private long saveCount;			//当前爬虫入库的数量
	
	private String cpu;				//cpu占用
	
	private String ram;				//内存占用
	
	private long totalCount;		//当天总数量
	
	private long totalSold;			//当天总销量
	
	private String exception;		//产生的异常
	
	public MonitorParam(long crawlerCount, long saveCount, String cpu, String ram) { //start + interval
		super();
		this.crawlerCount = crawlerCount;
		this.saveCount = saveCount;
		this.cpu = cpu;
		this.ram = ram;
	}
	
	public MonitorParam(long totalCount, long totalSold) { //daily
		super();
		this.totalCount = totalCount;
		this.totalSold = totalSold;
	}

	public MonitorParam(String exception) {
		super();
		this.exception = exception;
	}

	public long getCrawlerCount() {
		return crawlerCount;
	}

	public void setCrawlerCount(long crawlerCount) {
		this.crawlerCount = crawlerCount;
	}

	public long getSaveCount() {
		return saveCount;
	}

	public void setSaveCount(long saveCount) {
		this.saveCount = saveCount;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getRam() {
		return ram;
	}

	public void setRam(String ram) {
		this.ram = ram;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public long getTotalSold() {
		return totalSold;
	}

	public void setTotalSold(long totalSold) {
		this.totalSold = totalSold;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}
}
