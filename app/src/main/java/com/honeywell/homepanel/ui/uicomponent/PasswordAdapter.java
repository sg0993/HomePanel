package com.honeywell.homepanel.ui.uicomponent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.honeywell.homepanel.R;

/**
 * Created by H135901 on 2/17/2017.
 */
public class PasswordAdapter extends BaseAdapter{

    private AdapterCallback mAdapterCallback = null;
    private Context mContext;
    private LayoutInflater inflater = null;
    int [] mImages = null;
    int [] mImages_down = null;
    public PasswordAdapter(Context c,AdapterCallback adapterCallbacks,int[] images,int[] images_down) {
        mAdapterCallback = adapterCallbacks;
        this.mImages = images;
        this.mImages_down = images_down;
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
            viewHolder = new ViewHolder(position,(Button) view.findViewById(R.id.backgroundimage));
            viewHolder.mBackgroundBtn.setBackgroundResource(mImages[position]);
            view.setTag(viewHolder);
        } else {
            view =  convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        return view;
    }

    class ViewHolder{
        private Button mBackgroundBtn = null;
        private int mPostion = 0;

        public ViewHolder(final int postion, Button backgroundBtn) {
            mPostion = postion;
            this.mBackgroundBtn = backgroundBtn;
            mBackgroundBtn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        mBackgroundBtn.setBackgroundResource(mImages_down[mPostion]);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP ||
                            motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                        mBackgroundBtn.setBackgroundResource(mImages[mPostion]);
                        mAdapterCallback.subviewOnclick(mPostion,"");
                    }
                    return true;
                }
            });
        }
    }
}
