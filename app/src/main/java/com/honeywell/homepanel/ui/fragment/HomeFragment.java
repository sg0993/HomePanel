package com.honeywell.homepanel.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.ui.activities.ScenarioSelectActivity;
import com.honeywell.homepanel.ui.uicomponent.PageViewAdapter;

/**
 * Created by H135901 on 1/25/2017.
 */

@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment implements View.OnClickListener{
    private String title = "";
    private PageViewAdapter mPageAdaper = null;
    private ImageView choose_scenarioImageView = null;
    private Context mContext = null;
    public HomeFragment(String title) {
        super();
        this.title = title;
    }
    public HomeFragment() {
        super();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_home, null);
        mPageAdaper = new PageViewAdapter(view,getActivity(),R.layout.mainpage01,R.layout.mainpage02,
                R.id.pagerRootId,R.id.dotViewRoot,R.id.pageView);

        initViews();
        return view;
    }

    private void initViews() {
        View mainPage1 = mPageAdaper.pageViews.get(0);
        choose_scenarioImageView = (ImageView) mainPage1.findViewById(R.id.choose_scenarioImage);
        choose_scenarioImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId){
            case R.id.choose_scenarioImage:
                showScenarioSelect();
                break;
            default:
                break;
        }
    }
    private void showScenarioSelect() {
        startActivity(new Intent(mContext, ScenarioSelectActivity.class));
    }

}
