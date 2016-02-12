package com.cxhd.freight;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能主类(主体部分)
 * Created by Li on 2016/1/5.
 */
public class MainActivity extends BaseActivity {
    private TextView main_title;
    private List<View> tabList = new ArrayList<View>();
    private View main_map_btn;
    private int currentTab = -1;
    private GoodsListViewFragment goodsListViewFragment;
    private MyInvoice myInvoice;
    private SettingFragment mySet;
    private MapMain mapMain;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        main_map_btn = (View) findViewById(R.id.mapBtn);
        main_title = (TextView) findViewById(R.id.head_title);
        tabList.add((LinearLayout) findViewById(R.id.main_tab_btn_1));
        tabList.add((LinearLayout) findViewById(R.id.main_tab_btn_2));
        tabList.add((LinearLayout) findViewById(R.id.main_tab_btn_4));
        tabList.add((LinearLayout) findViewById(R.id.main_tab_btn_5));
        intent = getIntent();
        currentTab = (int) intent.getIntExtra("com.cxhd.freight.tabid", -1);
        setNowPage();
    }
    public void onClick_Event(View view){
        int checkedId = view.getId();
        switch (checkedId) {
            case R.id.main_tab_btn_1:
                //首页
                intent = new Intent(this,HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case  R.id.main_tab_btn_2:
                //找货
                currentTab = R.id.main_tab_btn_2;
                setNowPage();
                break;
            case  R.id.main_tab_btn_3:
                //我的状态
                break;
            case  R.id.main_tab_btn_4:
                //我的运单
                currentTab = R.id.main_tab_btn_4;
                if(AppConfig.getLandingState()){
                    setNowPage();
                }else{
                    landing();
                }
                break;
            case  R.id.main_tab_btn_5:
                //我的设置
                currentTab = R.id.main_tab_btn_5;
                if(AppConfig.getLandingState()){
                    setNowPage();
                }else{
                    landing();
                }
                break;
            case R.id.mapBtn:
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                if(mapMain==null){
                    mapMain = new MapMain();
                }
                transaction.replace(R.id.main_frame_content, mapMain);
                transaction.commit();
                break;
        }
    }
    public void setNowPage(){
        main_map_btn.setVisibility(View.GONE);
        for(int i=0;i<tabList.size();i++){
            View vi = tabList.get(i);
            vi.setSelected(false);
        }
        View nowTabBtn = (View) findViewById(currentTab);
        nowTabBtn.setSelected(true);
        // 开启Fragment事务
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (currentTab) {
            case R.id.main_tab_btn_1:
                //首页
                break;
            case  R.id.main_tab_btn_2:
                //找货
                if (goodsListViewFragment == null)
                {
                    goodsListViewFragment = new GoodsListViewFragment();
                }
                // 使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.main_frame_content, goodsListViewFragment);
                main_title.setText(R.string.main_tab_name_searchGoods);
                main_map_btn.setVisibility(View.VISIBLE);
                transaction.commit();
                break;
            case  R.id.main_tab_btn_3:
                //我的状态
                break;
            case  R.id.main_tab_btn_4:
                //我的运单
                if (myInvoice == null)
                {
                    myInvoice = new MyInvoice();
                }
                transaction.replace(R.id.main_frame_content, myInvoice);
                main_title.setText(R.string.main_tab_name_waybill);
                transaction.commit();
                break;
            case  R.id.main_tab_btn_5:
                //我的设置
                if(mySet==null){
                    mySet = new SettingFragment();
                }
                transaction.replace(R.id.main_frame_content, mySet);
                main_title.setText(R.string.main_tab_name_my);
                transaction.commit();
                break;
        }

    }
    /*
    * 判断是否登陆（未登陆跳转到登陆界面）
    */
    public void landing(){
        Intent mainAC = new Intent(getApplicationContext(),Landing.class);
        startActivity(mainAC);
        this.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }
    @Override
    public void onResume() {
        super.onResume();
    }
}
