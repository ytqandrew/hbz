package com.cxhd.freight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 个人信息组件
 * Created by Li on 2016/1/5.
 */
public class UserInfo extends Activity {
    private Context mContext = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);
        mContext = this;

        TextView backBtn = (TextView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                UserInfo.this.finish();
            }
        });
//        Button callPhone = (Button) findViewById(R.id.call_phone);
//        callPhone.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telString));
//                //通知activtity处理传入的call服务
//                //WaybillDetail.this.startActivity(intent);
//                startActivity(intent);
//            }
//        });
    }
    public void onClick_Event(View view){
        Intent intent = null;
        MyPopupWindow addPopWindow = null;
        int checkedId = view.getId();
        switch (checkedId) {
            case R.id.user_name:
                //个人信息
                intent = new Intent(this,UserInfo.class);
                break;
            case  R.id.user_city:
                //城市信息
                intent = new Intent(this,CitySetting.class);
                intent.putExtra("com.cxhd.freight.type", (int) 0);
                break;
            case  R.id.user_qq:
                //qq账号设置
                intent = new Intent(this,TextSetting.class);
                intent.putExtra("com.cxhd.freight.type", (int) 0);
                break;
            case  R.id.user_ofFirm:
                //所属公司
                intent = new Intent(this,TextSetting.class);
                intent.putExtra("com.cxhd.freight.type", (int) 1);
                break;
            case  R.id.user_phone:
                //随车电话
                intent = new Intent(this,TextSetting.class);
                intent.putExtra("com.cxhd.freight.type", (int) 2);
                break;
            case R.id.user_carType:
                //车辆类型设置
                addPopWindow = new MyPopupWindow(UserInfo.this,0);
                addPopWindow.showPopupWindow(view);
                break;
            case R.id.user_carExtent:
                //车辆长度设置
                addPopWindow = new MyPopupWindow(UserInfo.this,1);
                addPopWindow.showPopupWindow(view);
                break;
            case R.id.user_carLoad:
                //车辆载重设置
                addPopWindow = new MyPopupWindow(UserInfo.this,2);
                addPopWindow.showPopupWindow(view);
                break;
            default:
                Log.d("setting","其它");
                break;
        }
        if(intent!=null){
            startActivity(intent);
            this.overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
        }
    }
    protected  void onPause(){
        super.onPause();
        //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }
}
