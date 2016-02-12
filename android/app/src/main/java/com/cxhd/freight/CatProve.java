package com.cxhd.freight;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 车辆认证
 * Created by Li on 2016/1/11.
 */
public class CatProve extends Fragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.cat_prove, null);
        android.util.Log.d("mark", "onCreateView()--------->news Fragment");
        return view;
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        android.util.Log.d("mark", "onPause()--------->news Fragment");
    }
}
