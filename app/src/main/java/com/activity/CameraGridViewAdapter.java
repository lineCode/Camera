package com.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.camera.R;

import java.util.List;


/**
 *  展示交作业图片适配器
 */
public class CameraGridViewAdapter extends BaseAdapter {

	private Context mContext;
	private List<Bitmap> mBitmapList;

	public CameraGridViewAdapter(Context context) {
		this.mContext = context;
	}

	public List<Bitmap> getBitmapList() {
		return mBitmapList;
	}

	public void setBitmapList(List<Bitmap> bitmapList) {
		mBitmapList = bitmapList;
	}

	@Override
	public int getCount() {
		return mBitmapList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_submithw_gridview,null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_imageView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (position == mBitmapList.size()) {
			viewHolder.imageView.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_addpic_unfocused));
			if (position == 4) {
				viewHolder.imageView.setVisibility(View.GONE);
			}
		} else {
			viewHolder.imageView.setImageBitmap(mBitmapList.get(position));

		}

		return convertView;
	}

	static class ViewHolder {
		ImageView imageView;
	}

}