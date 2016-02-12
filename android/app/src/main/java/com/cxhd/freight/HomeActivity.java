package com.cxhd.freight;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.cxhd.freight.custom.image.ADInfo;
import com.cxhd.freight.custom.image.ImageCycleView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * ��ҳ����
 * Created by Li on 2016/1/28.
 */
public class HomeActivity extends BaseActivity {
    private ImageCycleView mAdView;
    private ArrayList<ADInfo> infos = new ArrayList<ADInfo>();
    private MainActivity mainActivity;
    private SettingFragment mySet;
    private String[] imageUrls = {"http://img.taodiantong.cn/v55183/infoimg/2013-07/130720115322ky.jpg",
            "http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg",
            "http://pic18.nipic.com/20111215/577405_080531548148_2.jpg"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_activity);
        for(int i=0;i < imageUrls.length; i ++){
            ADInfo info = new ADInfo();
            info.setUrl(imageUrls[i]);
            info.setContent("top-->" + i);
            infos.add(info);
        }
        mAdView = (ImageCycleView) findViewById(R.id.ad_view);
        mAdView.setImageResources(infos, mAdCycleViewListener);
    }
    private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {

        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            Toast.makeText(getApplication(), "content->" + info.getContent(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void displayImage(String imageURL, ImageView imageView) {
            ImageLoader.getInstance().displayImage(imageURL, imageView);// ʹ��ImageLoader��ͼƬ���м�װ��
        }
    };
    public void onClick_Event(View view){
        Intent intent = null;
        int checkedId = view.getId();
        switch (checkedId) {
            case R.id.home_btn_zh:
                //�һ�
                intent = new Intent(this,MainActivity.class);
                intent.putExtra("com.cxhd.freight.tabid",(int) R.id.main_tab_btn_2);
                break;
            case R.id.home_btn_bbx:
                //�ٱ���
                break;
            case R.id.home_btn_yd:
                //�ҵ��˵�
                if(AppConfig.getLandingState()){
                    intent = new Intent(this,MainActivity.class);
                    intent.putExtra("com.cxhd.freight.tabid",(int) R.id.main_tab_btn_4);
                }else{
                    landing();
                }
                break;
            case  R.id.home_btn_wd:
                //�ҵ�����
                if(AppConfig.getLandingState()){
                    intent = new Intent(this,MainActivity.class);
                    intent.putExtra("com.cxhd.freight.tabid",(int) R.id.main_tab_btn_5);
                }else{
                    landing();
                }
                break;
        }
        if(intent!=null){
            startActivity(intent);
        }
    }
    /*
    * �ж��Ƿ��½��δ��½��ת����½���棩
    */
    public void landing(){
        Intent mainAC = new Intent(getApplicationContext(),Landing.class);
        startActivity(mainAC);
        this.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mAdView.startImageCycle();
    };

    @Override
    protected void onPause() {
        super.onPause();
        mAdView.pushImageCycle();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdView.pushImageCycle();
    }
}
