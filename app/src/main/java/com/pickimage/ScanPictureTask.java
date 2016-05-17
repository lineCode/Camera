package com.pickimage;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 说明:利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
 * Author shaozucheng
 * Time:16/4/14 上午11:08
 */
public class ScanPictureTask extends AsyncTask<Void, Boolean, ScanPictureData> {
    Context mContext;
    ScanPictureCallBack mScanPictureCallBack;//扫描完成回调
    ScanPictureData scanPictureData = new ScanPictureData();//存放扫描数据的
    HashSet<String> mDirPaths = new HashSet<>();//临时的辅助类，用于防止同一个文件夹的多次扫描
    List<ImageFolder> mImageFolders = new ArrayList<>();//扫描拿到所有的图片文件夹
    File mPictureMaximumDir;//图片数量最多的文件夹
    int mPictureMaximumSize;//图片数量最多的文件的图片数量
    int mTotalCount = 0;//总数量
    ProgressDialog mProgressDialog;//进度条

    public ScanPictureTask(Context mContext, ScanPictureCallBack scanPictureCallBack) {
        this.mContext = mContext;
        mScanPictureCallBack = scanPictureCallBack;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // 显示进度条
        mProgressDialog = ProgressDialog.show(mContext, null, "正在加载...");
        mProgressDialog.show();
    }

    @Override
    protected ScanPictureData doInBackground(Void... params) {

        String firstImage = null;
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = mContext.getContentResolver();
        // 只查询jpeg和png的图片
        Cursor mCursor = mContentResolver.query(mImageUri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED);

        Log.e("TAG", mCursor.getCount() + "");
        while (mCursor.moveToNext()) {
            // 获取图片的路径
            String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            // Log.e("TAG", path);
            // 拿到第一张图片的路径
            if (firstImage == null)
                firstImage = path;
            // 获取该图片的父路径名
            File parentFile = new File(path).getParentFile();
            if (parentFile == null)
                continue;
            String dirPath = parentFile.getAbsolutePath();
            ImageFolder imageFolder = null;
            // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
            if (mDirPaths.contains(dirPath)) {
                continue;
            } else {
                mDirPaths.add(dirPath);
                // 初始化imageFolder
                imageFolder = new ImageFolder();
                imageFolder.setDir(dirPath);
                imageFolder.setFirstImagePath(path);
            }

            if (parentFile.list() == null) {//解决部分手机报空指针
                continue;
            }

            int picSize = parentFile.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg"))
                        return true;
                    return false;
                }
            }).length;
            mTotalCount += picSize;
            imageFolder.setCount(picSize);
            mImageFolders.add(imageFolder);

            if (picSize > mPictureMaximumSize) {
                mPictureMaximumSize = picSize;
                mPictureMaximumDir = parentFile;
            }
        }
        mCursor.close();

        scanPictureData.setImageFolders(mImageFolders);
        scanPictureData.setPictureMaximumDir(mPictureMaximumDir);
        scanPictureData.setPictureMaximumSize(mPictureMaximumSize);
        scanPictureData.setTotalCount(mTotalCount);

        return scanPictureData;
    }

    @Override
    protected void onPostExecute(ScanPictureData scanPictureData) {
        super.onPostExecute(scanPictureData);
        // 扫描完成，辅助的HashSet也就可以释放内存了
        mDirPaths = null;
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mScanPictureCallBack.ScanPictureComplete(scanPictureData);
    }

    public interface ScanPictureCallBack {
        void ScanPictureComplete(ScanPictureData scanPictureData);
    }
}
