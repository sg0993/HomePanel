package com.honeywell.homepanel.ui.uicomponent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.honeywell.homepanel.R;

/**
 * Created by H135901 on 2/16/2017.
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater = null;

    private int [] mTextes = null;
    private int[] mImages = null;

    public ImageAdapter(Context c,int[] images,int[]textes) {
        mImages = images;
        mTextes = textes;
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
        // TODO Auto-generated method stub
        View view;
        if (convertView == null) {
            //给ImageView设置资源
            view = inflater.inflate(R.layout.layout_scenarioselect_item, null);
        } else {
            view = convertView;
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImages[position]);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextes[position]);

        return view;
    }

}
