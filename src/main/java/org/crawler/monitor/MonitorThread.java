package org.crawler.monitor;

public class MonitorThread extends Thread{
	
	public static boolean flag ; //是否监控
	
	private int state = -1;        
	 
	private MonitorParam monitorParam;
	
	private int interval = 10;//默认10分钟  
	
	public MonitorThread(int state,MonitorParam monitorParam) {
		this.state = state;
		this.monitorParam = monitorParam;
	}
	
	public MonitorThread(int interval) {
		this.state = 1;
		this.interval = interval;
	}
	
	@Override
	public void run() {
		switch(state){
			case 0:
				String result = MonitorRequester.start(monitorParam);
				if("-1".equals(result)){
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					MonitorRequester.start(monitorParam); //发生错误再次尝试请求
				}
				break;
			case 1:
				while(flag){
					try {
						Thread.sleep(interval*60*1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(!flag){
						break;
					}
					monitorParam = MonitorExecute.getMsgParam();//每次更新参数
					MonitorRequester.sendMessage(monitorParam);
				}
				break;
			case 2:
				MonitorRequester.sendMessage(monitorParam); //结束时最后一次调msg
				break;
			case 3:
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MonitorRequester.sendDaily(monitorParam);//日报接口
				break;
			case 4:
				MonitorRequester.sendException(monitorParam);//错误通知接口
		}
	}

	
}
