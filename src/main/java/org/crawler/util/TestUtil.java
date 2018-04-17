package org.crawler.util;

import java.text.DecimalFormat;
import java.text.ParseException;

public class TestUtil {

    public static void main(String[] args) {
        String numContent = "百度为您找到相关结果约100,000,000个";
        DecimalFormat df=new DecimalFormat(",###,##0"); //没有小数
        Long num = null;
        try {
            num = df.parse(numContent.substring(numContent.indexOf("约")+1, numContent.indexOf("个"))).longValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(num);
    }
}
