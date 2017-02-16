package com.honeywell.homepanel.ui.activities;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.ui.uicomponent.ImageAdapter;

/**
 * Created by H135901 on 2/16/2017.
 */

public class ScenarioSelectActivity extends Activity {
    GridView gridView;
    private static  final  int [] TEXTES = {R.string.scenario_home,
            R.string.scenario_away,
            R.string.scenario_sleep,
            R.string.scenario_wakeup};
    private static  final  int[] IMAGES = {R.mipmap.home_sel,R.mipmap.away,R.mipmap.sleep,R.mipmap.wakeup};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_homescenarioselect);

        GridView gridView= (GridView) findViewById(R.id.gridView);

        gridView.setAdapter(new ImageAdapter(this, IMAGES,TEXTES));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub
                Toast.makeText(ScenarioSelectActivity.this, "你选择了"+(position+1)+"号图片", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
