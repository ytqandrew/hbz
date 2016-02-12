package com.cxhd.freight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cxhd.freight.utils.GoodsUtils;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

/**
 * 货单详情
 * Created by Li on 2015/12/23.
 */
public class WaybillDetail extends Activity {
    private Intent intent;
    private String goodsId;
    private List<HashMap<String,String>> dataList;
    private HashMap<String,String> dataObj;
    private String telString;
    private int source;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waybill_details);
        intent = getIntent();
        goodsId = intent.getStringExtra("com.cxhd.freight.goodsId");
        source = intent.getIntExtra("com.cxhd.freight.source",-1);
        if(source==0){
            dataObj = (HashMap<String,String>) MyManage.goodsData.get(goodsId);
        }else{
            dataObj = (HashMap<String,String>) GoodsUtils.map_goods_info;
        }
        telString = dataObj.get("s_goods_phone");
        initData();
        TextView backBtn = (TextView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                WaybillDetail.this.finish();
//                WaybillDetail.this.setResult(RESULT_OK, intent);
//                WaybillDetail.this.finish();
//                overridePendingTransition(R.anim.out_to_left, R.anim.in_from_right);
            }
        });
    }
    protected  void onPause(){
        super.onPause();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }
    private void initData(){
        if(dataObj!=null){
            TextView originAddressText = (TextView)this.findViewById(R.id.goods_origin_address);
            originAddressText.setText(GoodsUtils.getProvinceCityData(dataObj.get("s_address_start")));
            TextView endAddressText = (TextView)this.findViewById(R.id.goods_end_address);
            endAddressText.setText(GoodsUtils.getProvinceCityData(dataObj.get("s_address_end")));
            TextView desTimerText = (TextView)this.findViewById(R.id.goods_destination_timer);
            desTimerText.setText(MyManage.setTimerFormat(dataObj.get("i_start_time")));
            TextView goodsTypeText = (TextView)this.findViewById(R.id.goods_type);
            goodsTypeText.setText(AppConfig.goods_type_lst[Integer.parseInt(dataObj.get("i_goods_type"))]);
            TextView goodsWeightText = (TextView)this.findViewById(R.id.goods_weight);
            goodsWeightText.setText(GoodsUtils.getWeightData(dataObj.get("i_goods_weight_type"), dataObj.get("f_goods_weight")));
            TextView noteText = (TextView)this.findViewById(R.id.goods_note_info);
            noteText.setText(dataObj.get("s_goods_note"));
            TextView goodsTypeText2 =  (TextView)this.findViewById(R.id.goods_type_3);
            if(dataObj.get("i_goods_weight_type").equals("0")){
                goodsTypeText2.setText(MyManage.getStringResources(this,R.string.goods_type_0));
            }else
            {
                goodsTypeText2.setText(MyManage.getStringResources(this,R.string.goods_type_1));
            }
            TextView carTypeText = (TextView)this.findViewById(R.id.car_type);
            carTypeText.setText(AppConfig.car_type_lst[Integer.parseInt(dataObj.get("i_car_type"))]);
            TextView carExtentText = (TextView)this.findViewById(R.id.car_extent);
            carExtentText.setText(dataObj.get("f_car_length") + "米");
            TextView priceText = (TextView)this.findViewById(R.id.price);
            priceText.setText(GoodsUtils.getPriceData(dataObj.get("c_price")));
            TextView goodsHostText = (TextView)this.findViewById(R.id.goods_host);
            goodsHostText.setText("平台发货");
        }
    }
}
