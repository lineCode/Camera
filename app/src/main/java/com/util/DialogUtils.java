package com.util;

import android.app.Dialog;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created on 16/1/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class DialogUtils {
    /**
     * 设置Dialog在屏幕中的位置
     * @param dialog 设置的Dialog对象
     * @param gravity 设置Dialog的起始位置
     * @param xOffset 设置Dialog相对于起始位置的X偏移量
     * @param yOffset 设置Dialog相对于起始位置的Y偏移量
     */
    public static final void resetDialogScreenPosition(Dialog dialog, int gravity, int xOffset, int yOffset) {
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(gravity);
        lp.x = xOffset; // 新位置X坐标
        lp.y = yOffset; //新位置Y坐标
        dialogWindow.setAttributes(lp);
    }

    /**
     * 设置Dialog在屏幕中的位置
     * @param dialog 设置的Dialog对象
     * @param gravity 设置Dialog的起始位置
     * @param xOffset 设置Dialog相对于起始位置的X偏移量
     * @param yOffset 设置Dialog相对于起始位置的Y偏移量
     * @param width 设置Dialog的显示宽度
     * @param height 设置Dialog的显示高度
     */
    public static final void resetDialogScreenPosition(Dialog dialog, int gravity, int xOffset, int yOffset, int width, int height) {
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(gravity);
        lp.x = xOffset; // 新位置X坐标
        lp.y = yOffset; //新位置Y坐标
        lp.dimAmount = 0.4f;
        lp.width = width; // 宽度
        lp.height = height; // 高度
        dialogWindow.setAttributes(lp);
    }

    /**
     * 设置Dialog在屏幕中的位置
     * @param dialog 设置的Dialog对象
     * @param gravity 设置Dialog的起始位置
     * @param xOffset 设置Dialog相对于起始位置的X偏移量
     * @param yOffset 设置Dialog相对于起始位置的Y偏移量
     * @param width 设置Dialog的显示宽度
     * @param height 设置Dialog的显示高度
     * @param alpha 设置Dialog的透明度
     */
    public static final void resetDialogScreenPosition(Dialog dialog, int gravity, int xOffset, int yOffset, int width, int height, float alpha) {
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(gravity);
        lp.x = xOffset; // 新位置X坐标
        lp.y = yOffset; //新位置Y坐标
        lp.width = width; // 宽度
        lp.height = height; // 高度
        lp.alpha = alpha; // 透明度
        dialogWindow.setAttributes(lp);
    }

}
