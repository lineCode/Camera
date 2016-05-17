package com.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.camera.R;
import com.dialog.ImageDirListDialog;
import com.pickimage.CameraPhotoHelper;
import com.pickimage.ImageFolder;
import com.pickimage.MyAdapter;
import com.pickimage.ScanPictureData;
import com.pickimage.ScanPictureTask;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * 说明:从相册中选择图片
 * Author shaozucheng
 * Time:16/4/8 上午11:06
 */
public class AlbumActivity extends Activity implements ImageDirListDialog.OnImageDirSelected {
    private ImageView mLeftImageView;
    private TextView mCompleteTextView;
    private GridView mGirdView;//展示图片GirdView
    private TextView mChooseDir;//目录
    private TextView mImageCount;//图片数量
    private RelativeLayout mBottomLayout;//底部布局

    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFolder> mImageFolders = new ArrayList<>();
    /**
     * 存放图片的文件夹
     */
    private File mImgDir;
    /**
     *  图片文件夹下所有的图片
     */
    private List<String> mImgDirPicture;

    private MyAdapter mAdapter;//展示图片适配器
    private int mNeedSelectAmount = 1;
    private ImageDirListDialog mImageDirListDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        mNeedSelectAmount = getIntent().getIntExtra(CameraPhotoHelper.INTENT_KEY_NEED_SELECT_AMOUNT, 1);

        initView();
        getImageData();
        initBottomEvent();
        completeOnClickListener();
    }

    //初始化组件
    private void initView() {
        mLeftImageView = (ImageView) findViewById(R.id.screen_left_btn);
        mCompleteTextView = (TextView) findViewById(R.id.tv_screen_complete);
        mGirdView = (GridView) findViewById(R.id.album_gridView);
        mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
        mImageCount = (TextView) findViewById(R.id.id_total_count);
        mBottomLayout = (RelativeLayout) findViewById(R.id.id_bottom_layout);
        leftFinishActivity();
    }

    private void leftFinishActivity() {
        mLeftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void completeOnClickListener() {
        mCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> selectImage = mAdapter.getSelectedImage();
                if (selectImage.size() == 0) {
                    Toast.makeText(AlbumActivity.this, "您还没有选择图片", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent mIntent = new Intent();
                mIntent.putStringArrayListExtra(CameraPhotoHelper.INTENT_KEY_ALBUM, selectImage);
                setResult(RESULT_OK, mIntent);
                finish();
            }
        });
    }


    /**
     * 获取图片数据
     */
    private void getImageData() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        ScanPictureTask scanPictureTask = new ScanPictureTask(AlbumActivity.this, new ScanPictureTask.ScanPictureCallBack() {
            @Override
            public void ScanPictureComplete(ScanPictureData scanPictureData) {
                mImageFolders=scanPictureData.getImageFolders();
                mImgDir = scanPictureData.getPictureMaximumDir();
                initGridViewData();
            }
        });
        scanPictureTask.execute();
    }

    /**
     * 为GirdViewView绑定数据
     */
    private void initGridViewData() {
        if (mImgDir == null) {
            Toast.makeText(getApplicationContext(), "没有扫描到任何图片", Toast.LENGTH_SHORT).show();
            return;
        }
        mImgDirPicture = Arrays.asList(mImgDir.list());
        //把排序翻转，按时间最新的排序
        Collections.reverse(mImgDirPicture);
        //可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
        mAdapter = new MyAdapter(AlbumActivity.this, mImgDirPicture, R.layout.grid_item, mImgDir.getAbsolutePath(), mNeedSelectAmount);
        mGirdView.setAdapter(mAdapter);
        mImageCount.setText(mImgDirPicture.size() + "张");
        mChooseDir.setText(mImgDir.getName());
    }


    @Override
    public void selected(ImageFolder folder) {
        mImgDir = new File(folder.getDir());
        mImgDirPicture = Arrays.asList(mImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg"))
                    return true;
                return false;
            }
        }));
        //把排序翻转，按时间最新的排序
        Collections.reverse(mImgDirPicture);
        //可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
        mAdapter = new MyAdapter(getApplicationContext(), mImgDirPicture, R.layout.grid_item, mImgDir.getAbsolutePath(), mNeedSelectAmount);
        mGirdView.setAdapter(mAdapter);
        mImageCount.setText(folder.getCount() + "张");
        mChooseDir.setText(folder.getName());
        mImageDirListDialog.dismiss();
    }

    /**
     * 为底部的布局设置点击事件，弹出dialog
     */
    private void initBottomEvent() {
        mBottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initImageDirDialog();
            }
        });
    }

    /**
     * 初始化展示文件夹的dialog
     */
    private void initImageDirDialog() {
        mImageDirListDialog = new ImageDirListDialog(AlbumActivity.this, mImageFolders);
        // 设置选择文件夹的回调
        mImageDirListDialog.setOnImageDirSelected(this);
    }

}
