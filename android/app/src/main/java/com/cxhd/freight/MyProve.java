package com.cxhd.freight;

import android.app.Activity;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * 认证组件
 * Created by Li on 2016/1/11.
 */
public class MyProve extends FragmentActivity {
    private FragmentTabHost tabHost;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprove);
        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        //tabHost.getTabWidget().setDividerDrawable(null);
        tabHost.getTabWidget().setBackgroundResource(R.drawable.bodr_red);
        tabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        View indicator = null;
        indicator = getIndicatorView("实名认证", R.layout.page_host);
        tabHost.addTab(tabHost.newTabSpec("0").setIndicator(indicator), UserProve.class, null);
        indicator = getIndicatorView("车辆认证", R.layout.page_host);
        tabHost.addTab(tabHost.newTabSpec("1").setIndicator(indicator), CatProve.class, null);
    }
    private View getIndicatorView(String name, int layoutId) {
        View v = getLayoutInflater().inflate(layoutId, null);
        TextView tv = (TextView) v.findViewById(R.id.tabText);
        tv.setText(name);
        return v;
    }
}
