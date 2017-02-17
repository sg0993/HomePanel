package com.honeywell.homepanel.ui.activities;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.ui.uicomponent.PasswordAdapter;

/**
 * Created by H135901 on 2/16/2017.
 */

public class PasswordEnterActivity extends Activity {
    private GridView gridView;
    private static  final  int[] IMAGES = {R.mipmap.one,R.mipmap.two,R.mipmap.three,
                                            R.mipmap.four,R.mipmap.five,R.mipmap.six,
                                            R.mipmap.seven,R.mipmap.eight,R.mipmap.nine,
                                            R.mipmap.clear,R.mipmap.zero,R.mipmap.delete};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_passwordenter);

        GridView gridView= (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new PasswordAdapter(this, IMAGES));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                Toast.makeText(PasswordEnterActivity.this, "你选择了"+(position+1)+"号图片", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
