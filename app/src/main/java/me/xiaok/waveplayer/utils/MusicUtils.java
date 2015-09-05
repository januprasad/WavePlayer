package me.xiaok.waveplayer.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;

import me.xiaok.waveplayer.models.Song;

/**
 * Created by GeeKaven on 15/8/17.
 */
public class MusicUtils {

    /**
     * 将时长转化成 xx:xx:xx的String形式
     *
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
        } else if (!minute.equals("00")) {
            result.append(minute).append(":").append(second);
        } else {
            result.append(second);
        }

        return result.toString();
    }

    /**
     * Bitmap的倒影效果
     * <p/>
     * 1.创建一个原图像90度翻转的图像
     * 2.根据翻转后图像创建画布
     * 3.创建画笔，并设置线性渐变shader
     * 4.将画笔画在Canvas上
     *
     * @return 原图像带有阴影的倒影图像
     */
    public static Bitmap createReflectedImage(Bitmap originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        //90度翻转
        Matrix matrix = new Matrix();
        matrix.setScale(1, -1);
        //创建倒影图片，大小与原图相同
        Bitmap reflectedImage = Bitmap.createBitmap(originalImage, 0, 0, width, height, matrix, false);
        //创建画布
        Canvas canvas = new Canvas(reflectedImage);
        //创建画笔
        Paint shaderPaint = new Paint();
        //创建线性渐变LinearGradient对象
        LinearGradient shader = new LinearGradient(0, 0, 0, (2 * reflectedImage.getHeight()) / 3, 0x70ffffff, 0x10ffffff,
                Shader.TileMode.CLAMP);
        //将线性渐变效果画在Canvas上
        shaderPaint.setShader(shader);
        shaderPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0, 0, width, reflectedImage.getHeight(), shaderPaint);
        return reflectedImage;
    }

    /**
     * 设置铃声
     *
     * @param song
     */
    public static void setRingTone(Context context, Song song) {
        ContentValues cv = new ContentValues();
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(song.getmSongPath());

        //先检查媒体库中是否存在改音乐
        Cursor cursor = context.getContentResolver().query(
                uri,
                null,
                MediaStore.MediaColumns.DATA + "=?",
                new String[]{song.getmSongPath()},
                null
        );

        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            String _id = cursor.getString(0);

            //更新铃声ku
            cv.put(MediaStore.Audio.Media.IS_RINGTONE, true);

            context.getContentResolver().update(
                    uri,
                    cv,
                    MediaStore.MediaColumns.DATA + "=?",
                    new String[]{song.getmSongPath()}
            );

            Uri newUri = ContentUris.withAppendedId(uri, Long.valueOf(_id));

            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri);

            //播放铃声
//            Ringtone rt = RingtoneManager.getRingtone(context, newUri);
//            rt.play();
        }
    }
}
