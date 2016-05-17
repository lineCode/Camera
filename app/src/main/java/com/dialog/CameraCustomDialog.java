package com.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.camera.R;
import com.util.DialogUtils;
import com.util.NavigationBarUtil;


/**
 * 说明:选择拍照和从相册中选择图片对话框
 * Author shaozucheng
 * Time:16/3/4 上午9:36
 */
public class CameraCustomDialog {

    private Context mContext;
    private Dialog mDialog;
    private TextView mCancelButton;//取消按钮
    private TextView mPhotographTextView;//拍照
    private TextView mAlbumTextView;//从相册中选择

    public CameraCustomDialog(Context context) {
        this.mContext = context;
        mDialog = new android.app.AlertDialog.Builder(context, R.style.camera_dialog_no_screen).create();
        WindowManager wmManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        boolean hasSoft = NavigationBarUtil.hasSoftKeys(wmManager);//判断是否有虚拟键盘
        if (hasSoft) {
            int navigationBarHeight = NavigationBarUtil.getNavigationBarHeight(context);//获取虚拟键盘的高度
            //这一行很重要，将dialog对话框设置在虚拟键盘上面
            DialogUtils.resetDialogScreenPosition(mDialog, Gravity.BOTTOM, 0, navigationBarHeight, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
        } else {
            DialogUtils.resetDialogScreenPosition(mDialog, Gravity.BOTTOM, 0, 0, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
        }

        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.dialog_select_camera);
        window.setWindowAnimations(R.style.camera_dialog_enter_exit);  //添加dialog进入和退出的动画

        mCancelButton = (TextView) window.findViewById(R.id.dialog_cancel_btn);
        mPhotographTextView = (TextView) window.findViewById(R.id.dialog_photograph);
        mAlbumTextView = (TextView) window.findViewById(R.id.dialog_album);


    }

    public void setTextViewOnClickListener(View.OnClickListener listener) {
        if (mPhotographTextView != null) {
            mPhotographTextView.setOnClickListener(listener);
        }
        if (mAlbumTextView != null) {
            mAlbumTextView.setOnClickListener(listener);
        }
        if (mCancelButton != null) {
            mCancelButton.setOnClickListener(listener);
        }
    }


    /**
     * 设置是否可以关闭
     *
     * @param cancelable true或false
     */
    public void setCancelable(boolean cancelable) {
        mDialog.setCancelable(cancelable);
    }

    /**
     * 设置是否可以点击dialog外面关闭dialog
     *
     * @param canceledOnTouchOutside true或false
     */
    public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        mDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        mDialog.dismiss();
    }
}
