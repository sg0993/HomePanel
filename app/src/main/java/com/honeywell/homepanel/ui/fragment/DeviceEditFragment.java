package com.honeywell.homepanel.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.honeywell.homepanel.R;

/**
 * Created by H135901 on 1/25/2017.
 */

@SuppressLint("ValidFragment")
public class DeviceEditFragment extends Fragment {
    private String title = "";
    public DeviceEditFragment(String title) {
        super();
        this.title = title;

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deviceedit, null);
        return view;
    }

    /**
     * Created by H135901 on 1/25/2017.
     */

    @SuppressLint("ValidFragment")
    public static class DialFragment extends Fragment {
        /*@Bind(R.id.dialTv)
        TextView dialTv;*/
        private String title = "";

        public DialFragment(String title) {
            super();
            this.title = title;

        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_dial, null);
            

           /* ButterKnife.bind(this, view);

            dialTv.setText("Butter knift Test!!!");*/
            return view;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
    //        ButterKnife.unbind(this);
        }
    }
}
