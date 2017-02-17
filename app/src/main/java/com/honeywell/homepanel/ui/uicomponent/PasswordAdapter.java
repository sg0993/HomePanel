package com.honeywell.homepanel.ui.uicomponent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by H135901 on 2/17/2017.
 */
public class PasswordAdapter extends BaseAdapter {

    public static final int WIDTH = 150;
    private Context mContext;
    private LayoutInflater inflater = null;

    private int[] mImages = null;

    public PasswordAdapter(Context c, int[] images) {
        mImages = images;
        mContext = c;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //获取图片的数量
    @Override
    public int getCount() {
        return mImages.length;
    }

    //获取图片在库中的位置
    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView view;
        if (convertView == null) {
            //给ImageView设置资源
            view = new ImageView(mContext);
            view.setLayoutParams(new AbsListView.LayoutParams(WIDTH,WIDTH));
        } else {
            view = (ImageView) convertView;
        }
        view.setImageResource(mImages[position]);
        return view;
    }

}
