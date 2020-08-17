/**
 *
 */
package com.example.testproject.util;


import java.text.SimpleDateFormat;

/**
 * Created by mike.li.
 */
public class TimeUtil {
    public final static String FORMAT_YEAR_MONTH_DAY_TIME = "yyyy-MM-dd HH:mm";
    private static SimpleDateFormat sdf;
    static {
        sdf = new SimpleDateFormat(FORMAT_YEAR_MONTH_DAY_TIME);
    }
    public static String longToString(long time, String formatType) {
        if(time==0)return "";
        sdf.applyPattern(formatType);
        return sdf.format(time);
    }
}
