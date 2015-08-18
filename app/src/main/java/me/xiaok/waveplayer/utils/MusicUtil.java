package me.xiaok.waveplayer.utils;

/**
 * Created by GeeKaven on 15/8/17.
 */
public class MusicUtil {

    /**
     * 将时长转化成 xx:xx:xx的String形式
     * @param duration
     * @return
     */
    public static String durationToString(long duration) {
        long time = duration / 1000;
        String hour = String.valueOf(time / 3600);
        String minute = String.valueOf(time % 3600 / 60);
        String second = String.valueOf(time % 3600 % 60);

        if (minute.length() < 2) {
            minute = "0" + minute;
        }
        if (second.length() < 2) {
            second = "0" + second;
        }

        StringBuilder result = new StringBuilder();
        if (!hour.equals("0")) {
            result.append(hour).append(":").append(minute).append(":").append(second);
        } else if (!minute.equals("00")){
            result.append(minute).append(":").append(second);
        } else {
            result.append(second);
        }

        return result.toString();
    }
}
