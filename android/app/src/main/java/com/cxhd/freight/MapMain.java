package com.cxhd.freight;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.cxhd.freight.data.CallBackData;
import com.cxhd.freight.utils.GoodsUtils;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 地图组件(调用高德地图API)
 * Created by Li on 2015/12/11.
 */
public class MapMain extends Fragment implements LocationSource,
        AMapLocationListener,OnGeocodeSearchListener,AMap.OnMapClickListener,
        AMap.OnMarkerClickListener,AMap.OnInfoWindowClickListener,AMap.InfoWindowAdapter,CallBackData {
    private AMap aMap;
    private MapView mapView;
    private HashMap<String, Object> goodsLstLatLng = null;
    private List<HashMap<String, String>> jsonData;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient aMapLocationClient;
    private AMapLocationClientOption aMapLocationClientOption;
    private GeocodeSearch geocodeSearch;
    private Marker currentMarker;
    private View currentInfoWindows;
    private View layoutView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        layoutView = inflater.inflate(R.layout.basicmap_activity, null);
        mapView = (MapView) layoutView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        init();
        return layoutView;
    }
    public void callBack(String mid,String state,String err_id,String msg){
        if(mid.equals("19")){
            setGoodsMarkerData(state, err_id, msg);
        }else{
            setGoodsData(state, err_id, msg);
        }
    }
    private void setGoodsMarkerData(String state,String err_id,String msg){
        String err_str = "";
        if(state.equals("1")){
            goodsLstLatLng = GoodsUtils.getGoodsLatLngList(msg);
            setGoodsMarker();
        }else{
            switch (err_id){
                case "1":
                    err_str = MyManage.getStringResources(getActivity(),R.string.error_3);
                    break;
            }
            Toast.makeText(getActivity(), err_str, Toast.LENGTH_SHORT).show();
        }
    }
    private void setGoodsData(String state,String err_id,String msg){
        String err_str = "";
        if(state.equals("1")){
            GoodsUtils.setMapGoodsInfo(msg);
            TextView title = (TextView) currentInfoWindows.findViewById(R.id.marker_title);
            TextView snippet = (TextView) currentInfoWindows.findViewById(R.id.marker_snippet);
            String vStartAddress = GoodsUtils.getProvinceCityData(GoodsUtils.map_goods_info.get("s_address_start"));
            String weight = GoodsUtils.getWeightData(GoodsUtils.map_goods_info.get("i_goods_weight_type"), GoodsUtils.map_goods_info.get("f_goods_weight"));
            String vDemandInfo = AppConfig.goods_type_lst[Integer.parseInt(GoodsUtils.map_goods_info.get("i_goods_type"))]   + "  " + weight + "  " + MyManage.setTimerFormat(GoodsUtils.map_goods_info.get("i_start_time"));
            title.setText(vStartAddress);
            snippet.setText(vDemandInfo);
        }else{
            switch (err_id){
                case "1":
                    err_str = MyManage.getStringResources(getActivity(),R.string.error_3);
                    break;
            }
            Toast.makeText(getActivity(), err_str, Toast.LENGTH_SHORT).show();
        }
    }
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMapLocationClient = new AMapLocationClient(getActivity());
            aMapLocationClientOption = new AMapLocationClientOption();
            //设置是否只定位一次,默认为false
            //aMapLocationClientOption.setOnceLocation(false);
            //设置定位间隔,单位毫秒,默认为2000ms
            aMapLocationClientOption.setInterval(5000);
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            aMapLocationClient.setLocationListener(this);
            aMapLocationClient.startLocation();
            aMap.setLocationSource(this);
            aMap.setMyLocationEnabled(true);
            geocodeSearch = new GeocodeSearch(getActivity());
            geocodeSearch.setOnGeocodeSearchListener(this);
            searchLatLng();
            //104.06585,30.657361
            LatLng latLng = new LatLng(30.657361,104.06585);
            CameraPosition position=new CameraPosition(latLng,10, 0, 0);
            CameraUpdate cameraUpdate=  CameraUpdateFactory.newCameraPosition(position);
            aMap.moveCamera(cameraUpdate);
            aMap.setOnMapClickListener(this);
            // 设置amap加载成功事件监听器
            //mAMap.setOnMapLoadedListener(this);
            aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
            aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
            aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
            //setCenterPoi();
            //setUpMap();
        }
    }
    private void setGoodsMarker(){
        aMap.clear();
        Iterator iter = goodsLstLatLng.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            List<Double> val = (List<Double>)entry.getValue();
            double lng = val.get(0);
            double lat = val.get(1);
            LatLng latLng=new LatLng(lng,lat);
            MarkerOptions markerOptions = new MarkerOptions();
            // 设置Marker的图标样式
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_goods));
            // 设置Marker点击之后显示的标题
            markerOptions.title("").snippet("");
            // 设置Marker的坐标，为我们点击地图的经纬度坐标
            markerOptions.position(latLng);
            // 设置Marker的可见性
            markerOptions.visible(true);
            // 设置Marker是否可以被拖拽
            markerOptions.draggable(false);
            // 将Marker添加到地图上去
            Marker marker =  aMap.addMarker(markerOptions);
            marker.setObject(key);
        }
    }
    private void searchLatLng(){
        GeocodeQuery query = new GeocodeQuery("天府软件园C区", "028");
        geocodeSearch.getFromLocationNameAsyn(query);
    }
    private void setUpMap(){
        /*
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.mm_trans));
        myLocationStyle.strokeColor(Color.BLUE);
        myLocationStyle.strokeWidth(1);
        aMap.setMyLocationStyle(myLocationStyle);
        //mAMapLocManager =
        */
    }
    /*
     * 方法必须重写
    */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        MyManage.sendGoodListRequest("1", "1", "2", "1", this);
    }
    /*
     * 方法必须重写
    */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
    /*
     * 方法必须重写
    */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    /*
     * 方法必须重写
    */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != aMapLocationClient) {
            aMapLocationClient.onDestroy();
            aMapLocationClient = null;
            aMapLocationClientOption = null;
        }
    }
    /*
    * 返回定位结果
     */
    @Override
    public void onLocationChanged(AMapLocation aml) {
        if (mListener != null && aml != null) {
            if (aml != null && aml.getErrorCode() == 0) {
                mListener.onLocationChanged(aml);
            } else {
                String errText = "定位失败," + aml.getErrorCode()+ ": " + aml.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (null != onLocationChangedListener) {
            Log.d("MAP_MSG2", " " + onLocationChangedListener);
        }
    }
    /*
     * 停止定位
    */
    @Override
    public void deactivate() {

    }
    /*
    * 地理编码结果回调
    * 返回搜索地址的经纬度及位置描述
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        if (rCode == 0) {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size()>0) {
                GeocodeAddress address = result.getGeocodeAddressList().get(0);
                String addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
                        + address.getFormatAddress();
                Log.d("MAP_SEARCHED", ":" + addressName);
            }
        }
    }
    /*
     * 逆地理编码回调接口
     * 返回搜索坐标的地址信息
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {

    }
    /*
     * 点击非marker区域将显示的InfoWindow隐藏
     */
    @Override
    public void onMapClick(LatLng latLng) {
        if (currentMarker != null) {
            currentMarker.hideInfoWindow();
            currentMarker = null;
        }
    }
    /*
     * marker点击事件
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        currentMarker = marker;
        return false;
    }
    /*
     * InfoWindow窗口点击事件
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(getActivity(),WaybillDetail.class);
        intent.putExtra("com.cxhd.freight.source",(int) 1);
        intent.putExtra("com.cxhd.freight.goodsId", (String) marker.getObject());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        if (currentMarker != null) {
            currentMarker.hideInfoWindow();
            currentMarker = null;
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        //Log.d("MAP_OBJ"," "+obj.get("release_timer"));
        View infoWindow = getActivity().getLayoutInflater().inflate(R.layout.map_infowindow, null);
        TextView title = (TextView) infoWindow.findViewById(R.id.marker_title);
        TextView snippet = (TextView) infoWindow.findViewById(R.id.marker_snippet);
        title.setText("数据加载中...");
        snippet.setText("");
        currentInfoWindows = infoWindow;
        MyManage.sendGoodInfoRequest((String)marker.getObject(),this);
//        TextView title = (TextView) infoWindow.findViewById(R.id.marker_title);
//        TextView snippet = (TextView) infoWindow.findViewById(R.id.marker_snippet);
//
//        title.setText("数据");
//        snippet.setText("");
        return infoWindow;
    }
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}

