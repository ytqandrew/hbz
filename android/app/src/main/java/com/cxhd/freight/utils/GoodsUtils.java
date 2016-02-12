package com.cxhd.freight.utils;

import com.cxhd.freight.AppConfig;
import com.cxhd.freight.ReadJson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Li on 2016/1/26.
 */
public class GoodsUtils {

    public static HashMap<String,String> map_goods_info = null;

    /*
    * 省市编码转换
    * s_id(省市编码)
    * return address 省市文本
    */
    public static String getProvinceCityData(String s_id){
        String address = "";
        int pIndex = Integer.parseInt(s_id.split("_")[0]);
        int cIndex = Integer.parseInt(s_id.split("_")[1]);
        String cityString = "";
        if(AppConfig.set_city_lst[pIndex][cIndex].equals("全境")){
            cityString = AppConfig.set_city_lst[pIndex][cIndex];
        }else{
            cityString = AppConfig.set_city_lst[pIndex][cIndex] + "市";
        }
        address = AppConfig.set_province_lst[pIndex] + "  " + cityString;
        return address;
    }
    /*
    * 运费转换
    * p_num(运费)
    * return price 运费文本
    */
    public static String getPriceData(String p_num){
        String price = "";
        if(p_num.equals("0")){
            price = "面议";
        }else {
            price = p_num;
        }
        return price;
    }
    /*
    * 货物计量单位转换
    * w_type(计量类型)
    * w_num(数量ֵ)
    * return weight 计量文本
    */
    public static String getWeightData(String w_type,String w_num){
        String weight = "";
        if(w_type.equals("0")){
            weight = w_num + "吨";
        }else{
            weight = w_num + "方";
        }
        return weight;
    }
    /*
    * 获取货单地图列表数据
    * return hashMap--key,坐标信息(数组)
    */
    public static HashMap<String,Object> getGoodsLatLngList(String msg){
        HashMap<String, Object> tempLatLngList = new HashMap<String, Object>();
        List<String> tempInitLst = ReadJson.setListData(msg);
        List<String> tempKeyLst = ReadJson.setListData(tempInitLst.get(0));
        int goodsIdIndex = tempKeyLst.indexOf("i_id");
        for(int i=1;i<tempInitLst.size();i++){
            String tempGoodsText = tempInitLst.get(i);
            List<String> tempGoodsList = ReadJson.setListData(tempGoodsText);
            List<Double> tempLatLng = new ArrayList<Double>();
            for(int e=0;e<tempKeyLst.size();e++){
                if(tempKeyLst.get(e).equals("s_address_start_location")){
                    String tempString = tempGoodsList.get(e);
                    tempLatLng.add(Double.valueOf(tempString.split(",")[0]));
                    tempLatLng.add(Double.valueOf(tempString.split(",")[1]));
                }
            }
            tempLatLngList.put(tempGoodsList.get(goodsIdIndex),tempLatLng);
        }
        return tempLatLngList;
    }
    /*
    * 设置地图货单点击数据
    */
    public static void setMapGoodsInfo(String msg){
        GoodsUtils.map_goods_info = new HashMap<String,String>();
        List<String> tempInitLst = ReadJson.setListData(msg);
        List<String> tempKeyLst = ReadJson.setListData(tempInitLst.get(0));
        List<String> tempGoodsLst = ReadJson.setListData(tempInitLst.get(1));
        for(int i=0;i<tempKeyLst.size();i++){
            GoodsUtils.map_goods_info.put(tempKeyLst.get(i), tempGoodsLst.get(i));
        }
    }
}
