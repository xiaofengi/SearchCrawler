package org.crawler.processor.baidu;

import org.crawler.crawler.DatumGenerator;
import org.crawler.processor.Processor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.ParseException;

@Component
public class BaiduSearchProcessor implements Processor{

	@Resource
	private DatumGenerator datumGenerator;

	@Override
	public void process(Page page, CrawlDatums next) {
		int size = 0;
		//System.out.println(page.getHtml());
		Element contentLeft = page.select("#content_left").first();
		Elements divs = contentLeft.getElementsByClass("c-container");
		for(Element element:divs){
			Elements tmp = element.getElementsByAttributeValue("target", "_blank");
			if(tmp != null){
				Element aTag = tmp.first();
				String resultTitle = aTag.text();//搜索结果展示标题
				String href = aTag.attr("href");//搜索结果链接
				//System.out.println(href);
				//百度搜索结果url过滤
				if(href!=null && href.startsWith("http")){
					next.add(datumGenerator.generateBaiduSearchRs(href));
				}
				size++;
			}
		}
		//System.out.println("共搜索到" + size + "条数据");
		int pn = Integer.parseInt(page.meta("pn"));
		if(pn == 0){
			String numContent = page.select(".nums").first().text();
			System.out.println(numContent);
			DecimalFormat df = new DecimalFormat(",###,##0"); //没有小数
			int num = 0;
			try {
				num = df.parse(numContent.substring(numContent.indexOf("约")+1, numContent.indexOf("个"))).intValue();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			/*for(int i=10; i<=num; i+=10){ //抓取下一页
				next.add(datumGenerator.generateBaiduSearchList(page.meta("keyword"), i));
			}*/
		}
	}

}
