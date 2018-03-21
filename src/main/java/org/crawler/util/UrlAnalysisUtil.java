package org.crawler.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.google.common.base.Splitter;

public class UrlAnalysisUtil {
    /**
     * 去掉url中的路径，留下请求参数部分
     * @param strURL url地址
     * @return url请求参数部分
     */
    private static String TruncateUrl(String strURL) {
    	strURL = strURL.trim().toLowerCase();
    	List<String> arrSplit = Splitter.on("?").omitEmptyStrings().splitToList(strURL);
    	if(strURL.length() > 1 && arrSplit.size() > 1 && arrSplit.get(1) != null) {
    		return arrSplit.get(1);
    	}
    	return null;
      }

    /**
     * 解析出url参数中的键值对
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     * @param URL  url地址
     * @return  url请求参数部分
     */
    public static Map<String, String> analysisParam(String URL) {
    	Map<String, String> mapRequest = new HashMap<String, String>();
    	String strUrlParam = TruncateUrl(URL); 
    	if(strUrlParam == null) {
    		return mapRequest;
    	}
    	Iterable<String> arrSplit = Splitter.on("&").omitEmptyStrings().split(strUrlParam); 
    	for(String strSplit:arrSplit) {
    		List<String> arrSplitEqual = Splitter.on("=").omitEmptyStrings().splitToList(strSplit);
    		if(arrSplitEqual.size() > 1) {
    			mapRequest.put(arrSplitEqual.get(0), arrSplitEqual.get(1));
    		} else {
    			if(!StringUtils.isEmpty(arrSplitEqual.get(0))) {
    				mapRequest.put(arrSplitEqual.get(0), "");
    			}
    		}
    	}
    	return mapRequest;    
    }
    
    public static String getQueryStringParam(String url, String key) {
		Map<String, String> params = UrlAnalysisUtil.analysisParam(url);
		return params.get(key);
	}
    
} 
