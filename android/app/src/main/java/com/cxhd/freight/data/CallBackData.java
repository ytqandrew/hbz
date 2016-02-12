package com.cxhd.freight.data;

/**
 * 通信回调接口
 * Created by Li on 2016/1/20.
 */
public interface CallBackData {
    public void callBack(String mid,String state,String err_id,String msg);
}
