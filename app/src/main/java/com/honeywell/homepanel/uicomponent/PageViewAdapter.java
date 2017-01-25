package com.honeywell.homepanel.uicomponent;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.honeywell.homepanel.R;

import java.util.ArrayList;

/**
 * Created by H135901 on 1/24/2017.
 */

public class PageViewAdapter {

    public static final int DOT_WIDTH = 30;
    public static final int DOT_HEIGHT = DOT_WIDTH;

    private static  final  String TAG = "PageViewAdapter";

    private int mCurPageIndex = 0;
    private ViewPager viewPager;
    /**装分页显示的view的数组*/
    private ArrayList<View> pageViews;
    private ImageView imageView;
    /**将小圆点的图片用数组表示*/
    private ImageView[] imageViews;
    //包裹滑动图片的LinearLayout
    private ViewGroup viewPics;
    //包裹小圆点的LinearLayout
    private ViewGroup viewPoints;



    public PageViewAdapter(View view,Activity activity,int page1ResId, int page2ResId,
                           int frameLayoutId, int dotId, int pageViewId) {
        init(view,activity,page1ResId,page2ResId,frameLayoutId,dotId,pageViewId);
    }

    public void init(View view,Activity activity,int page1ResId,int page2ResId,int frameLayoutId,int dotId,int pageViewId) {
        //将要分页显示的View装入数组中
        pageViews = new ArrayList<View>();
        LayoutInflater inflater = activity.getLayoutInflater();
        View pageView1 = inflater.inflate(page1ResId,null);
        pageViews.add(pageView1);
        View pageView2 = inflater.inflate(page2ResId,null);
        pageViews.add(pageView2);

        //创建imageviews数组，大小是要显示的图片的数量
        imageViews = new ImageView[pageViews.size()];
        //从指定的XML文件加载视图
        //viewPics = (ViewGroup) inflater.inflate(R.layout.main, null);
        viewPics = (ViewGroup) view.findViewById(frameLayoutId);

        //实例化小圆点的linearLayout和viewpager
        viewPoints = (ViewGroup) viewPics.findViewById(dotId);
        viewPager = (ViewPager) viewPics.findViewById(pageViewId);

        //添加小圆点的图片
        for(int i = 0;i < pageViews.size();  i++){
            imageView = new ImageView(activity);
            //设置小圆点imageview的参数
            imageView.setLayoutParams(new ViewGroup.LayoutParams(DOT_WIDTH,DOT_HEIGHT));//创建一个宽高均为20 的布局
            imageView.setPadding(DOT_WIDTH, 0, DOT_HEIGHT, 0);
            //将小圆点layout添加到数组中
            imageViews[i] = imageView;

            //默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
            if(i==0){
                imageViews[i].setBackgroundResource(R.drawable.page_indicator_focused);
            }else{
                imageViews[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
            //将imageviews添加到小圆点视图组
            viewPoints.addView(imageViews[i]);
        }
        viewPager.setAdapter(new GuidePageAdapter());
        viewPager.setOnPageChangeListener(new GuidePageChangeListener());
    }

    class GuidePageAdapter extends PagerAdapter {
        //销毁position位置的界面
        @Override
        public void destroyItem(View v, int position, Object arg2) {
            // TODO Auto-generated method stub
            ((ViewPager)v).removeView(pageViews.get(position));
        }
        @Override
        public void finishUpdate(View arg0) {
        }
        @Override
        public int getCount() {
            return pageViews.size();
        }
        //初始化position位置的界面
        @Override
        public Object instantiateItem(View v, int position) {
            ((ViewPager) v).addView(pageViews.get(position));
            Log.d(TAG, "instantiateItem() position="+position);
            return pageViews.get(position);
        }
        // 判断是否由对象生成界面
        @Override
        public boolean isViewFromObject(View v, Object arg1) {
            return v == arg1;
        }
        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
        @Override
        public Parcelable saveState() {
            return null;
        }
    }
    class GuidePageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[position].setBackgroundResource(R.drawable.page_indicator_focused);
                //不是当前选中的page，其小圆点设置为未选中的状态
                if (position != i) {
                    imageViews[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
                }
            }
            Log.d(TAG, "onPageSelected() position=" + position);
            mCurPageIndex = position;
        }
    }
}
