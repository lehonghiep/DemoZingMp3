package com.honghiep.demoappzingmp3.util;

import java.text.SimpleDateFormat;

/**
 * Created by honghiep on 26/07/2017.
 */

public class Util {
    public static String milliSecondsToTimer(long milliseconds){
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        return format.format(milliseconds);
    }
    public static int getProgressPerentage(long currentDuration, long totalDuration){
        return (int) (100*currentDuration/totalDuration);
    }
    public static int progressToTimer(int progress, int totalDuration){
        return progress*totalDuration/100;
    }
}
