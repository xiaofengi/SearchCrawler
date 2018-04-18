package org.crawler.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestUtil {

    public static void main(String[] args) {
        String timeStr = "2017-08-19 10:15:44";
        if(timeStr.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}.*")){
            System.out.println(true);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date d = sdf.parse(timeStr);
            System.out.println(sdf.format(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
