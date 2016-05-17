package com.pickimage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/4/14 上午10:33
 */
public class ScanPictureData {
    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFolder> mImageFolders = new ArrayList<>();
    /**
     * 图片数量最多的文件夹
     */
    private File mPictureMaximumDir;
    /**
     * 图片数量最多的文件的图片数量
     */
    private  int mPictureMaximumSize;

    /**
     * 图片总数量
     */
    private int mTotalCount;

    public List<ImageFolder> getImageFolders() {
        return mImageFolders;
    }

    public void setImageFolders(List<ImageFolder> imageFolders) {
        mImageFolders = imageFolders;
    }

    public File getPictureMaximumDir() {
        return mPictureMaximumDir;
    }

    public void setPictureMaximumDir(File pictureMaximumDir) {
        mPictureMaximumDir = pictureMaximumDir;
    }

    public int getPictureMaximumSize() {
        return mPictureMaximumSize;
    }

    public void setPictureMaximumSize(int pictureMaximumSize) {
        mPictureMaximumSize = pictureMaximumSize;
    }

    public int getTotalCount() {
        return mTotalCount;
    }

    public void setTotalCount(int totalCount) {
        mTotalCount = totalCount;
    }
}
