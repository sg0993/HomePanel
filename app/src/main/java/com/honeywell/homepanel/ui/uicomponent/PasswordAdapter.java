package com.honeywell.homepanel.ui.uicomponent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.honeywell.homepanel.R;

/**
 * Created by H135901 on 2/17/2017.
 */
public class PasswordAdapter extends BaseAdapter{

    private AdapterCallback mAdapterCallback = null;
    private Context mContext;
    private LayoutInflater inflater = null;
    int [] mImages = null;
    public PasswordAdapter(Context c,AdapterCallback adapterCallbacks,int[] images) {
        mAdapterCallback = adapterCallbacks;
        this.mImages = images;
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
            view = inflater.inflate(R.layout.item_passwordnumber, null);
            viewHolder = new ViewHolder(position,(Button) view.findViewById(R.id.backgroundimage),
                    (ImageView)view.findViewById(R.id.centerimage));
            view.setTag(viewHolder);
        } else {
            view =  convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        if(position == 9){
            viewHolder.mBackgroundBtn.setBackgroundResource(R.mipmap.clear_background);
        }
        else if(position == 11){
            viewHolder.mBackgroundBtn.setBackgroundResource(R.mipmap.delete_background);
        }
        viewHolder.mImageView.setImageResource(mImages[position]);

        return view;
    }

    class ViewHolder{
        private Button mBackgroundBtn = null;
        private ImageView mImageView = null;
        private int mPostion = 0;

        public ViewHolder(final int postion, Button mBackgroundBtn, ImageView mImageView) {
            mPostion = postion;
            this.mBackgroundBtn = mBackgroundBtn;
            this.mImageView = mImageView;
            mBackgroundBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   mAdapterCallback.subviewOnclick(postion,"");
                }
            });
        }
    }
}
