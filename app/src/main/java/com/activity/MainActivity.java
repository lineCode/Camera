package com.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.camera.R;
import com.dialog.CameraCustomDialog;
import com.pickimage.CameraPhotoHelper;
import com.util.ImageEnviromentUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mSingleTextView;
    private ImageView mImageView;
    private GridView mGridView;
    private CameraPhotoHelper mCameraPhotoHelper;

    private CameraGridViewAdapter mAdapter;
    private List<Bitmap> mBitmapList = new ArrayList<>();
    private static final int PICTURE_MAX = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intData();
        mCameraPhotoHelper = new CameraPhotoHelper(MainActivity.this);
        mAdapter = new CameraGridViewAdapter(this);
        mAdapter.setBitmapList(mBitmapList);
        mGridView.setAdapter(mAdapter);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mCameraPhotoHelper.onConfigurationChanged(newConfig);
    }

    private void intData() {
        initView();
        initViewClickListener();
    }

    private void initView() {
        mSingleTextView = (TextView) findViewById(R.id.single_camera);
        mImageView = (ImageView) findViewById(R.id.camera_image);
        mGridView = (GridView) findViewById(R.id.picture_gridView);
    }

    private void initViewClickListener() {
        mSingleTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mSingleTextView) {
            showCameraDialog();
        }
    }

    /**
     * 显示拍照和选择相册dialog
     */
    public void showCameraDialog() {
        final CameraCustomDialog cameraDialog = new CameraCustomDialog(MainActivity.this);
        cameraDialog.setTextViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.dialog_photograph://拍照
                        mCameraPhotoHelper.takePhotoFromCamera();
                        break;
                    case R.id.dialog_album://从相册中选择
                        mCameraPhotoHelper.selectMoreFormAlbum(PICTURE_MAX - mBitmapList.size());
                        break;
                    case R.id.dialog_cancel_btn:
                        cameraDialog.dismiss();
                        break;
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCameraPhotoHelper.onActivityResult(requestCode, resultCode, data, new CameraPhotoHelper.CameraPhotoCallBack() {
            @Override
            public void takePictureFromCamera(String imagePath) {
                Bitmap mBitmap = ImageEnviromentUtil.compressImageSize(imagePath);
//                 mImageView.setImageBitmap(mBitmap);
                if (mBitmapList.size() < PICTURE_MAX) {
                    mBitmapList.add(mBitmap);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "最多需要4张图片", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void takePictureFromGallery(ArrayList<String> imagePathList) {
                List<Bitmap> bitmapList = ImageEnviromentUtil.getAlbumBitmapList(imagePathList);
                mBitmapList.addAll(bitmapList);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

}
