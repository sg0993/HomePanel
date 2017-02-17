package com.honeywell.homepanel.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.ui.uicomponent.ImageAdapter;
import com.honeywell.homepanel.ui.uicomponent.PageViewAdapter;

/**
 * Created by H135901 on 1/25/2017.
 */

@SuppressLint("ValidFragment")
public class ScenarioEditFragment extends Fragment {
    private String title = "";
    private PageViewAdapter mPageAdaper = null;
    private GridView choose_armImageView = null;
    private Context mContext = null;
    GridView gridView;
    private static final int[] IMAGES = {R.mipmap.secnario_disarm3x, R.mipmap.secnario_arm3x, R.mipmap.secnario_sleep3x, R.mipmap.secnario_wakeup3x};
    private static final int[] TEXTES = {R.string.secnario_disarm, R.string.secnario_arm, R.string.scenario_sleep, R.string.scenario_wakeup};

    public ScenarioEditFragment(String title) {
        super();
        this.title = title;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_scenarioedit, null);
        gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(getActivity(), IMAGES, TEXTES));

   //     initViews();

        return view;
    }

    private void initViews() {
        View senarioeedit = mPageAdaper.pageViews.get(0);
        choose_armImageView = (GridView) senarioeedit.findViewById(R.id.gridView);

    }
}
