package com.cxhd.freight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

/**
 * Created by Li on 2015/12/22.
 */
public class ReadJson {
    public static HashMap<String, String> setData(String str) {
        try {
            JSONObject jsonObject= new JSONObject(str);
            Iterator<?> it = jsonObject.keys();
            HashMap<String, String> map = new HashMap<String, String>();
            while (it.hasNext()){
                String key = (String)it.next();
                map.put(key, jsonObject.getString(key));
            }
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static List<HashMap<String, String>> setMapData(String str) {
        List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        try {
            JSONArray array = new JSONArray(str);
            int len = array.length();
            for (int i = 0; i < len; i++) {
                HashMap<String, String> map = setData(array.getString(i));
                data.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
    public static List<String> setListData(String str) {
        List<String> data = new ArrayList<String>();
        try {
            JSONArray array = new JSONArray(str);
            int len = array.length();
            for (int i = 0; i < len; i++) {
                data.add(array.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
