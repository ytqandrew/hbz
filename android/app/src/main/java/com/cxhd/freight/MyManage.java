package com.cxhd.freight;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceActivity;
import android.text.TextUtils;
import android.util.Log;

import com.cxhd.freight.data.CallBackData;
import com.cxhd.freight.service.HttpUtils;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.LogRecord;

/**
 * Created by Li on 2015/12/22.
 */
public class MyManage  {
    public static List<HashMap<String, String>> jsonData;
    private static List<String> goodsListKey = null;
    public static HashMap<String, Object> goodsData = new HashMap<String,Object>();
    private static Handler msgHandler = new Handler() {
        public void handleMessage(Message msg){
            CallBackData tempCallBack = (CallBackData) msg.obj;
            Bundle tempBundle = msg.getData();
            tempCallBack.callBack(tempBundle.getString("mid"), tempBundle.getString("start"), tempBundle.getString("errId"), tempBundle.getString("parmesList"));
        }
    };

    public static void initJsonData(Context context){
        String jsonStr = getJson(context,"data.json");
        jsonData = ReadJson.setMapData(jsonStr);
    }
    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    public static void setGoodsListData(String textMsg){
        List<String> tempInitLst = ReadJson.setListData(textMsg);
        if(MyManage.goodsListKey==null){
            MyManage.goodsListKey = ReadJson.setListData(tempInitLst.get(0));
        }
        int goodsIdIndex = MyManage.goodsListKey.indexOf("i_id");
        for(int i=1;i<tempInitLst.size();i++){
            String tempGoodsText = tempInitLst.get(i);
            List<String> tempGoodsList = ReadJson.setListData(tempGoodsText);
            HashMap<String,String> tempDataMap = new HashMap<String,String>();
            for(int e=0;e<MyManage.goodsListKey.size();e++){
                tempDataMap.put(MyManage.goodsListKey.get(e),tempGoodsList.get(e));
            }
            MyManage.goodsData.put(tempGoodsList.get(goodsIdIndex),tempDataMap);
        }
    }
    /*
    * 请求验证消息
    * type--验证类型
    * phone--验证电话号码
    * callBackData--验证回调
    */
    public static void sendCodeRequest(String type,String phone,final CallBackData callBackData){
        String[] temp_list = {"hbz","1",AppConfig.SERVICE_TOKEN,"",type,phone};
        Map<String,String> temp_map = MyManage.setRequestParmes("1", temp_list);
        MyManage.getGOSTHttpRquest(callBackData,temp_map);
    }
    /*
    * 请求注册消息
    * type--验证类型
    * phone--验证电话号码
    * code--验证码（写死1234）
    * password--密码
    * realname--真实用户名称
    * car_identity--车牌号（车主版使用）
    * callBackData--验证回调
    */
    public static void sendRegRequest(String type,String phone,String code,String password,String realname,String car_identity,final CallBackData callBackData){
        String[] temp_list = {"hbz","2",AppConfig.SERVICE_TOKEN,"",type,phone,code,password,realname,car_identity};
        Map<String,String> temp_map = MyManage.setRequestParmes("2", temp_list);
        MyManage.getGOSTHttpRquest(callBackData,temp_map);
    }
    /*
    * 请求登陆消息
    * type--验证类型
    * phone--验证电话号码
    * password--密码
    * callBackData--验证回调
    */
    public static void sendLoginRequest(String type,String phone,String password,final CallBackData callBackData){
        String[] temp_list = {"hbz","4",AppConfig.SERVICE_TOKEN,"",type,phone,password};
        Map<String,String> temp_map = MyManage.setRequestParmes("4", temp_list);
        MyManage.getGOSTHttpRquest(callBackData, temp_map);
    }
    /*
    * 请求货单详情消息
    * goods_id--货单ID
    * callBackData--验证回调
    */
    public static void sendGoodInfoRequest(String goods_id,final CallBackData callBackData){
        String[] temp_list = {"hbz","15",AppConfig.SERVICE_TOKEN,"",goods_id};
        Map<String,String> temp_map = MyManage.setRequestParmes("15", temp_list);
        MyManage.getGOSTHttpRquest(callBackData, temp_map);
    }
    /*
    * 请求货单列表消息
    * type--请求类型(0文本列表1地图列表)
    * start--货单起点
    * end--货单终点
    * page--页码
    * callBackData--验证回调
    */
    public static void sendGoodListRequest(String type,String start,String end,String page,final CallBackData callBackData){
        String[] temp_list = {"hbz","19",AppConfig.SERVICE_TOKEN,"",type,start,end,page};
        Map<String,String> temp_map = MyManage.setRequestParmes("19", temp_list);
        MyManage.getGOSTHttpRquest(callBackData, temp_map);
    }
    /*
    * 设置请求参数
    * mid--请求消息ID
    * s_list--发送消息参数
    */
    private static Map<String,String> setRequestParmes(String mid,String[] s_list){
        Map<String,String> parmes = new HashMap<String, String>();
        parmes.put("pid","hbz");
        parmes.put("mid", mid);
        JSONArray jsonArray = new JSONArray();
        //String [] pA = {"hbz","1",AppConfig.SERVICE_TOKEN,"","1","13369853247"};
        for (int i=0;i<s_list.length;i++){
            jsonArray.put((String)s_list[i]);
        }
        String paraS = jsonArray.toString();
        String sing = encryptToSHA(paraS);
        parmes.put("sign",sing);
        parmes.put("params", paraS);
        return parmes;
    }
    private static void getGOSTHttpRquest(final CallBackData callBackData, final Map<String,String> parmes){
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Looper.prepare();
                String requestCallBack = HttpUtils.performPostRequest(AppConfig.HTTP_LINK, parmes);
                if(requestCallBack!=null){
                    List<String> temp_json = ReadJson.setListData(requestCallBack);
                    //callBackData.callBack(temp_json.get(0),temp_json.get(1),temp_json.get(2),temp_json.get(4));
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = callBackData;
                    Bundle bundleData = new Bundle();
                    bundleData.putString("mid", temp_json.get(0));
                    bundleData.putString("start", temp_json.get(1));
                    bundleData.putString("errId", temp_json.get(2));
                    bundleData.putString("parmesList", temp_json.get(4));
                    message.setData(bundleData);
                    MyManage.msgHandler.sendMessage(message);
                    Log.d("httpCallBack", "request:" + requestCallBack);
                }
                Looper.loop();
            }
        }).start();
    }
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs;
    }
    /**
     * SHA1 加密
     */
    public static String encryptToSHA(String info) {
        info += AppConfig.SERVICE_SHAKEY;
        byte[] digesta = null;
        try {
            // 得到一个SHA-1的消息摘要
            MessageDigest alga = MessageDigest.getInstance("SHA-1");
            // 添加要进行计算摘要的信息
            alga.update(info.getBytes());
            // 得到该摘要
            digesta = alga.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 将摘要转为字符串
        String rs = byteArrayToHexString(digesta);
        return rs;
    }
    // 将字节转换为十六进制字符串
    private static String byteToHexString(byte ib) {
        char[] Digit = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c',
                'd', 'e', 'f'
        };
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];
        String s = new String(ob);
        return s;
    }
    // 将字节数组转换为十六进制字符串
    private static String byteArrayToHexString(byte[] bytearray) {
        String strDigest = "";
        for (int i = 0; i < bytearray.length; i++) {
            strDigest += byteToHexString(bytearray[i]);
        }
        return strDigest;
    }
    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }
    /**
     * 获取字符串资源
     */
    public static String getStringResources(Context context,int id){
        String temp_string = context.getString(id);
        return temp_string;
    }
    /**
     * 时间戳转换
     */
    public static String setTimerFormat(String data){
        String time = "";
        String beginDate=data;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        time = sdf.format(new Date(Long.parseLong(beginDate)*1000));
        return time;
    }
}
