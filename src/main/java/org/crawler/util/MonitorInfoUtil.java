package org.crawler.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MonitorInfoUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(MonitorInfoUtil.class);
	
	private static boolean isLinux;
	
	@Value("${crawler.monitor.isLinux}")
	public void setLinux(boolean isLinux) {
		MonitorInfoUtil.isLinux = isLinux;
	}

	/**
	 * Linux下内存信息
	 * 
	 * @return
	 */
	public static String getMemMsg() {
		if(!isLinux){
			return "0.5";
		}
		logger.info("开始收集memory使用率");
		float memUsage = 0.0f;
		Process pro = null;
		Runtime r = Runtime.getRuntime();
		try {
			String command = "cat /proc/meminfo";
			pro = r.exec(command);
			BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			String line = null;
			int count = 0;
			long totalMem = 0, freeMem = 0;
			while ((line = in.readLine()) != null) {
				logger.info(line);
				String[] memInfo = line.split("\\s+");
				if (memInfo[0].startsWith("MemTotal")) {
					totalMem = Long.parseLong(memInfo[1]);
				}
				if (memInfo[0].startsWith("MemFree")) {
					freeMem = Long.parseLong(memInfo[1]);
				}
				memUsage = 1 - (float) freeMem / (float) totalMem;
				logger.info("本节点内存使用率为: " + memUsage);
				if (++count == 2) {
					break;
				}
			}
			in.close();
			pro.destroy();
		} catch (IOException e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			logger.error("MemUsage发生InstantiationException. " + e.getMessage());
			logger.error(sw.toString());
		}
		DecimalFormat df = new DecimalFormat("0.0000");  
		String result = df.format(memUsage);      
		return result;
	}

	/**
	 * linux下 Purpose:采集CPU使用率
	 * 
	 * @param args
	 * @return float,CPU使用率,小于1
	 */
	public static String getCpuMsg() {
		if(!isLinux){
			return "0.5";
		}
		logger.info("开始收集cpu使用率");
		float cpuUsage = 0;
		Process pro1, pro2;
		Runtime r = Runtime.getRuntime();
		try {
			String command = "cat /proc/stat";
			// 第一次采集CPU时间
			long startTime = System.currentTimeMillis();
			pro1 = r.exec(command);
			BufferedReader in1 = new BufferedReader(new InputStreamReader(pro1.getInputStream()));
			String line = null;
			long idleCpuTime1 = 0, totalCpuTime1 = 0; // 分别为系统启动后空闲的CPU时间和总的CPU时间
			while ((line = in1.readLine()) != null) {
				if (line.startsWith("cpu")) {
					line = line.trim();
					logger.info(line);
					String[] temp = line.split("\\s+");
					idleCpuTime1 = Long.parseLong(temp[4]);
					for (String s : temp) {
						if (!s.equals("cpu")) {
							totalCpuTime1 += Long.parseLong(s);
						}
					}
					logger.info("IdleCpuTime: " + idleCpuTime1 + ", " + "TotalCpuTime" + totalCpuTime1);
					break;
				}
			}
			in1.close();
			pro1.destroy();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				logger.error("CpuUsage休眠时发生InterruptedException. " + e.getMessage());
				logger.error(sw.toString());
			}
			// 第二次采集CPU时间
			long endTime = System.currentTimeMillis();
			pro2 = r.exec(command);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(pro2.getInputStream()));
			long idleCpuTime2 = 0, totalCpuTime2 = 0; // 分别为系统启动后空闲的CPU时间和总的CPU时间
			while ((line = in2.readLine()) != null) {
				if (line.startsWith("cpu")) {
					line = line.trim();
					logger.info(line);
					String[] temp = line.split("\\s+");
					idleCpuTime2 = Long.parseLong(temp[4]);
					for (String s : temp) {
						if (!s.equals("cpu")) {
							totalCpuTime2 += Long.parseLong(s);
						}
					}
					logger.info("IdleCpuTime: " + idleCpuTime2 + ", " + "TotalCpuTime" + totalCpuTime2);
					break;
				}
			}
			if (idleCpuTime1 != 0 && totalCpuTime1 != 0 && idleCpuTime2 != 0 && totalCpuTime2 != 0) {
				cpuUsage = 1 - (float) (idleCpuTime2 - idleCpuTime1) / (float) (totalCpuTime2 - totalCpuTime1);
				logger.info("本节点CPU使用率为: " + cpuUsage);
			}
			in2.close();
			pro2.destroy();
		} catch (IOException e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			logger.error("CpuUsage发生InstantiationException. " + e.getMessage());
			logger.error(sw.toString());
		}
		DecimalFormat df = new DecimalFormat("0.0000");
		String result = df.format(cpuUsage);      
		return result;
	}

}