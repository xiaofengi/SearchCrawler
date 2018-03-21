package org.crawler.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	public static String readFile(File file) {
		StringBuilder sb = new StringBuilder();
		if(!file.exists()) {
			logger.error(file.getName() + " not exists");
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			String tempString = null;  
	        while ((tempString = reader.readLine()) != null) {
	            sb.append(tempString);
	        }
	        reader.close();
		} catch (Exception e) {
			
		}
        return sb.toString();
 	}
	
	public static void deleteFile(File file) {
		if(!file.exists()) {
			logger.error("delete file: " + file.getName() + " not exists");
		}
		boolean deleted = file.delete();
		if(!deleted) {
			logger.error(file.getName() + " delete error");
		}
	}
}
