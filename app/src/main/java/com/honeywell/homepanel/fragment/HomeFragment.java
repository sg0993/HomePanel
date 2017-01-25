package com.honeywell.homepanel.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.uicomponent.PageViewAdapter;

/**
 * Created by H135901 on 1/25/2017.
 */

@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment {
    private String title = "";
    private PageViewAdapter mPageAdaper = null;
    public HomeFragment(String title) {
        super();
        this.title = title;

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, null);
        mPageAdaper = new PageViewAdapter(view,getActivity(),R.layout.mainpage01,R.layout.mainpage02,
                R.id.pagerRootId,R.id.dotViewRoot,R.id.pageView);




        return view;
    }

}
