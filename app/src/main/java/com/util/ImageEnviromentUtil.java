package com.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/4/7 下午2:07
 */
public class ImageEnviromentUtil {

    public static File getScreenshot() {
        return new File(Environment.getExternalStorageDirectory() + "/shaozucheng/");
    }

    public static String getFileName() {
        return String.valueOf((new Date()).getTime()) + ".JPEG";
    }

    /**
     * 按照图片的尺寸 压缩图片
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static Bitmap compressImageSize(String path) {
        File file = new File(path);
        FileInputStream fileInputStream = null;
        Bitmap bitmap = null;
        try {
            fileInputStream = new FileInputStream(file);
            BufferedInputStream in = new BufferedInputStream(fileInputStream);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int i = 0;
            while (true) {
                if ((options.outWidth >> i <= 1024) && (options.outHeight >> i <= 1024)) {
                    in = new BufferedInputStream(new FileInputStream(new File(path)));
                    options.inSampleSize = (int) Math.pow(2.0D, i);
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeStream(in, null, options);
                    Log.e("TTTT", options.outWidth + "");
                    Log.e("TTTT", options.outHeight + "");
                    break;
                }
                i += 1;
            }
            int degree = getBitmapDegree(path);//查看图片是否被选择过（三星手机）
            if (degree != 0) {
                bitmap = rotateBitmapByDegree(bitmap, degree);//将图片选择回来
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    public static Bitmap decodeImage(String imagePath, int maxNumOfPixels) {
        Bitmap bitmap;
        if (TextUtils.isEmpty(imagePath))
            return null;
        // 缩放图片, width, height 按相同比例缩放图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        // options 设为true时，构造出的bitmap没有图片，只有一些长宽等配置信息，但比较快，设为false时，才有图片
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        options.inSampleSize = computeInitialSampleSize(options, -1, maxNumOfPixels);
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        int degree = getBitmapDegree(imagePath);
        if (degree != 0) {
            bitmap = rotateBitmapByDegree(bitmap, degree);
        }
        return bitmap;
    }

    /**
     * 根据给定图片的大小计算图片的缩放比率
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    private static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static List<Bitmap> getAlbumBitmapList(List<String> imagePathList) {
        List<Bitmap> bitmapList = new ArrayList<>();
        for (int i = 0; i < imagePathList.size(); i++) {
            Bitmap bitmap = compressImageSize(imagePathList.get(i));
            bitmapList.add(bitmap);
        }
        return bitmapList;
    }
}
