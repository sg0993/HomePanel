package com.honeywell.homepanel.ui.uicomponent;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.honeywell.homepanel.R;

/**
 * Created by H135901 on 2/20/2017.
 */

public class CustomProgressBar extends ProgressBar{
    public CustomProgressBar(Context context)
    {
        super(context);
        init();
    }

    public CustomProgressBar(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        init();
    }

    public CustomProgressBar(Context context, AttributeSet attributeSet, int defStyle){
        super(context, attributeSet, defStyle);
        init();
    }

    private void init(){
        setProgressDrawable(getResources().getDrawable(R.drawable.progressbar));
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
    }

    public void setProgress(int progress){
        super.setProgress(progress);
        invalidate();
    }
}
