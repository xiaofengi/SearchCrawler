/**
 * 
 */
package org.crawler.pipeline;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.crawler.listener.CrawlerBeginListener;
import org.crawler.listener.CrawlerEndListener;
import org.crawler.monitor.MonitorExecute;
import org.crawler.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;

public abstract class DatabasePipeline implements Pipeline, CrawlerEndListener, CrawlerBeginListener{
	private static final Logger logger = LoggerFactory.getLogger(DatabasePipeline.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
	
	private static final String FOLDER = "json/json"+sdf.format(new Date());
	private static final long THREAD_SLEEP = 5000l;
	private static final int THREAD_NUM = 10;
	
	private Vector<Object> objects = new Vector<Object>();
	
	private static final ExecutorService objectsTaskExec = Executors.newFixedThreadPool(1);
	private static final ExecutorService fileTaskExec = Executors.newFixedThreadPool(THREAD_NUM);
	private static final ExecutorService databaseTaskExec = Executors.newFixedThreadPool(THREAD_NUM);
	
	private boolean running = false;
	
	@Override
	public void handleResult(Object obj) {
		objects.add(obj);
		MonitorExecute.counter.incrementAndGet();
	}
	
	@Override
	public void crawlerBegin() {
		checkJsonFolderEnable();
		execObjectTask();
	}
	
	private void checkJsonFolderEnable() {
		File file = new File(FOLDER);
		if(!file.exists()) {
			file.mkdir();
		} else {
			for(File child: file.listFiles()) {
				child.delete();
			}
		}
	}
	
	private void execObjectTask() {
		this.running = true;
		objectsTaskExec.execute(new ObjectsTask());
	}
	
	@Override
	public void crawlerEnd() {
		shutdownObjectsTaskExec();
		shutdownFileTaskExec();
		shutdownDatabaseTaskExec();
	}
	
	private void shutdownObjectsTaskExec() {
		while (objects.size() > 0) {
			try {
				Thread.sleep(THREAD_SLEEP);
			} catch (InterruptedException e) {
			}
		}
		objectsTaskExec.shutdown();
		this.running = false;
	}
	
	private void shutdownFileTaskExec() {
		fileTaskExec.shutdown();		
	}

	private void shutdownDatabaseTaskExec() {
		while (MonitorExecute.fileCounter.get() > 0) {
			logger.info("hbasebaseCount: " + MonitorExecute.fileCounter.get());
			logger.info("counter: " + MonitorExecute.counter.get());
			try {
				Thread.sleep(THREAD_SLEEP);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
		}

		databaseTaskExec.shutdown();
		
		File file = new File(FOLDER);
		if(file.isDirectory()) {
			 File[] files = file.listFiles();
			 if(files == null || files.length == 0) {
				 file.delete();
			 }
		}
	}
	
	private File getJsonFile(String filename) {
		return new File(FOLDER + File.separator + filename);
	}
	
	protected abstract void write2database(String content);

	class DatabaseTask implements Runnable {
		private String filename;
		
		public DatabaseTask(String filename) {
			this.filename = filename;
		}

		@Override
		public void run() {
			String content = FileUtil.readFile(getJsonFile(filename));
			write2database(content);
			databaseSerializableDone(filename);
		}
		
		private void databaseSerializableDone(String filename) {
			FileUtil.deleteFile(getJsonFile(filename));
			MonitorExecute.fileCounter.decrementAndGet();
		}
	}
	
	class FileTask implements Runnable {
		private List<Object> goodsItem;
		
		public FileTask(List<Object> goodsItem) {
			this.goodsItem = goodsItem;
		}

		@Override
		public void run() {
			if(goodsItem.size() > 0) {
				 try {
					String filename = UUID.randomUUID().toString();
					Gson gson = new Gson();
					PrintWriter printWriter = new PrintWriter(new FileWriter(getJsonFile(filename)));
					printWriter.write(gson.toJson(goodsItem));
					printWriter.flush();
		            printWriter.close();
					
					databaseTaskExec.execute(new DatabaseTask(filename));
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
	}
	
	class ObjectsTask implements Runnable {
		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			while(running) {
				try {
					logger.info("counter: " + MonitorExecute.counter.get());
					Thread.sleep(THREAD_SLEEP);
				} catch (InterruptedException e) {
				}
				synchronized (objects) {
					if(objects.size() > 0) {
						MonitorExecute.fileCounter.incrementAndGet();
						fileTaskExec.execute(new FileTask((List<Object>)objects.clone()));
						objects.clear();
					}
				}
			}
		}
	}
}