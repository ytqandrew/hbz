package com.cxhd.freight;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.view.ViewGroup.LayoutParams;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * type popwindow 打开类型
 * Created by Li on 2016/1/14.
 */
public class MyPopupWindow extends PopupWindow implements AdapterView.OnItemClickListener {
    private View conentView;
    private Context parent;
    private int msgType;

    public MyPopupWindow(final Activity context,int type) {
        // 一个自定义的布局，作为显示的内容
        parent = context;
        msgType = type;
        conentView = LayoutInflater.from(context).inflate(R.layout.pop, null);
        GridView gridView = (GridView) conentView.findViewById(R.id.gridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        addTwoClassify(gridView);
        gridView.setOnItemClickListener(this);
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.FILL_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.FILL_PARENT);
        this.setTouchable(true);
        ColorDrawable dw = new ColorDrawable(-00000 );
        this.setBackgroundDrawable(dw);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        View addTaskLayout = conentView.findViewById(R.id.pop_bg);
        addTaskLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                MyPopupWindow.this.dismiss();
            }
        });
    }
    private void addTwoClassify(GridView gridView){
        ArrayList hashlist=new ArrayList<HashMap<String,Object>>();
        String[] tempLst = null;
        if(msgType==0){
            tempLst = AppConfig.car_type_lst;
        }else if (msgType==1){
            tempLst = AppConfig.car_length_lst;
        }else if(msgType==2){
            tempLst = new String[70];
            for (int e=1;e<71;e++){
                tempLst[e-1] = e + "吨";
            }
        }
        for(int i=0;i<tempLst.length;i++){
            HashMap<String, Object> map= new HashMap<String, Object>();
            map.put("fenlei_btnlist_btn_text", tempLst[i]);
            hashlist.add(map);
        }
        SimpleAdapter sAdapter=new SimpleAdapter(parent,hashlist,R.layout.pop_btn, new String[]{"fenlei_btnlist_btn_text"},new int[]{R.id.pop_btn_text});
        gridView.setAdapter(sAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int idx, long arg3) {
        //点击Item后获取点击数据
        //Log.d("popwindow","carType:"+AppConfig.car_length_lst[idx]);
        showPopupWindow(arg1);
    }
    /**
     * 显示popupWindow
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAtLocation(parent, Gravity.CENTER, 0,0);
        } else {
            this.dismiss();
        }
    }
}
