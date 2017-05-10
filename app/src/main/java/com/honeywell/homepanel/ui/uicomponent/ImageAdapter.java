package com.honeywell.homepanel.ui.uicomponent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    private AdapterCallback mCallBacks = null;

    private int [] mTextes = null;
    private int[] mImages = null;
    private int[] mImages_down = null;

    public ImageAdapter(Context c,AdapterCallback callbacks,int[] images,int[] images_down,int[]textes) {
        mImages = images;
        mCallBacks = callbacks;
        mImages_down = images_down;
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
        View view;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            //给ImageView设置资源
            view = inflater.inflate(R.layout.layout_scenarioselect_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
            TextView textView = (TextView) view.findViewById(R.id.textview);
            viewHolder = new ViewHolder(position,imageView,textView);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mImageView.setImageResource(mImages[position]);
        viewHolder.mTextView.setText(mTextes[position]);
        return view;
    }

    class ViewHolder{
        private ImageView mImageView = null;
        private   TextView mTextView = null;
        private int mPostion = 0;

        public ViewHolder(final int postion, ImageView imageView, TextView textView) {
            mPostion = postion;
            this.mImageView = imageView;
            this.mTextView = textView;

            mImageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        mImageView.setImageResource(mImages_down[mPostion]);
                        mTextView.setTextColor(mContext.getResources().getColor(R.color.black));
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP
                            || motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        mImageView.setImageResource(mImages[mPostion]);
                        mCallBacks.subviewOnclick(mPostion,"");
                        mTextView.setTextColor(mContext.getResources().getColor(R.color.black_text_transparent));
                    }
                    return true;
                }
            });
        }
    }

}
