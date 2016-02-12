package com.cxhd.freight;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cxhd.freight.data.CallBackData;
import com.cxhd.freight.utils.GoodsUtils;
import com.cxhd.freight.view.XListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 运单组件
 * Created by Li on 2016/1/11.
 */
public class MyInvoice extends Fragment implements XListView.IXListViewListener,CallBackData {
	private FragmentTabHost tabHost;
	private View layoutView;
	private Bundle bundle;
	private XListView mListView;
	private MyAdatper mAdapter;
	private ArrayList<HashMap<String,String>> items = new ArrayList<HashMap<String,String>>();
	private int indexPage = 1;
	private View pageBtn1;
	private View pageBtn2;
	private View pageBtn3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//bundle = savedInstanceState;
		super.onCreate(savedInstanceState);

	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
		layoutView = inflater.inflate(R.layout.myinvoice, null);
		pageBtn1 = (View)layoutView.findViewById(R.id.page_1);
		pageBtn2 = (View)layoutView.findViewById(R.id.page_2);
		pageBtn3 = (View)layoutView.findViewById(R.id.page_3);

		View.OnClickListener clickEvent = new View.OnClickListener(){
			public void onClick(View view){
				pageBtn1.setSelected(false);
				pageBtn2.setSelected(false);
				pageBtn3.setSelected(false);
				int checkedId = view.getId();
				switch (checkedId) {
					case R.id.page_1:
						//个人信息
						pageBtn1.setSelected(true);
						break;
					case  R.id.page_2:
						pageBtn2.setSelected(true);
						break;
					case  R.id.page_3:
						pageBtn3.setSelected(true);
						break;
				}
			}
		};

		pageBtn1.setOnClickListener(clickEvent);
		pageBtn2.setOnClickListener(clickEvent);
		pageBtn3.setOnClickListener(clickEvent);
		pageBtn1.setSelected(true);
		init();
		return layoutView;
	}
	private void init(){
		mListView = (XListView) layoutView.findViewById(R.id.invoiceView);
		mListView.setPullLoadEnable(true);
		mAdapter = new MyAdatper(getActivity(),R.layout.goods_item,items);
		//mListView.setAdapter(mAdapter);
		mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
//		mListView.setPullLoadEnable(false);
//		mListView.setPullRefreshEnable(false);
		mListView.setXListViewListener(this);
		MyManage.sendGoodListRequest("0", "1", "2", String.valueOf(indexPage), this);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(arg0.getContext(),WaybillDetail.class);
				intent.putExtra("com.cxhd.freight.source",(int) 0);
				intent.putExtra("com.cxhd.freight.goodsId", (String) arg1.getTag());
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			}
		});
	}
	private void geneItems() {
		Iterator iter = MyManage.goodsData.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			HashMap<String, String> val = (HashMap<String, String>)entry.getValue();
			if(val.get("i_state").equals("0")){
				items.add((HashMap<String, String>) val);
			}
			//Log.d("goodsList", "" + val);
		}
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}

	//刷新列表从第一页开始
	@Override
	public void onRefresh() {
		indexPage = 1;
		MyManage.sendGoodListRequest("0","1","2",String.valueOf(indexPage),this);
	}

	//加载更多
	@Override
	public void onLoadMore() {
		geneItems();
		mAdapter.notifyDataSetChanged();
		onLoad();
//		mHandler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				geneItems();
//				mAdapter.notifyDataSetChanged();
//				onLoad();
//			}
//		}, 2000);
	}

	public void callBack(String mid,String state,String err_id,String msg){
		String err_str = "";
		if(state.equals("1")){
			if(msg.equals("")){
				//货单列表为空
				onLoad();
			}else{
				//有货单列表
				MyManage.setGoodsListData(msg);
				HashMap<String,Object> tempMap = MyManage.goodsData;
			}
			items.clear();
			geneItems();
			// mAdapter.notifyDataSetChanged();
			mAdapter = new MyAdatper(getActivity(),R.layout.goods_item,items);
			mListView.setAdapter(mAdapter);
			onLoad();
		}else{
			switch (err_id){
				case "1":
					err_str = MyManage.getStringResources(getActivity(),R.string.error_3);
					break;
			}
			Toast.makeText(getActivity(), err_str, Toast.LENGTH_SHORT).show();
		}
	}
	class MyAdatper extends ArrayAdapter<HashMap<String,String>> {
		private int resource;
		public MyAdatper(Context c, int r,  List<HashMap<String,String>> items){
			super(c, r, items);
			this.resource = r;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.size();
			//return dataList.size();
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}
		/**
		 * @param position      数组下标
		 * @param contentView   item容器
		 */
		@Override
		public View getView(int position, View contentView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			HashMap<String,String> data = getItem(position);
			if(contentView==null){
				contentView = LayoutInflater.from(getContext()).inflate(R.layout.goods_item, null);
				render(data,contentView);
			}else if(contentView.getTag()!=data.get("i_id"))
			{
				render(data,contentView);
			}
			return contentView;
		}
		private void render(HashMap<String,String> data,View contentView){
			String vShipmentTimer = MyManage.setTimerFormat(data.get("i_start_time"));
			String vStartAddress = GoodsUtils.getProvinceCityData(data.get("s_address_start"));
			String vEndAddress = GoodsUtils.getProvinceCityData(data.get("s_address_end"));
			String weight = GoodsUtils.getWeightData(data.get("i_goods_weight_type"), data.get("f_goods_weight"));
			String vDemandInfo = AppConfig.goods_type_lst[Integer.parseInt(data.get("i_goods_type"))]   + "  " + weight + "  " + AppConfig.car_type_lst[Integer.parseInt(data.get("i_car_type"))];
			String vNoteInfo = data.get("s_goods_note");
			String vCost = GoodsUtils.getPriceData(data.get("c_price"));
			String vId = data.get("i_id");
			contentView.setTag(vId);
			TextView costText =  (TextView)contentView.findViewById(R.id.goods_cost);
			TextView shipmentTimerText =  (TextView)contentView.findViewById(R.id.goods_destination_timer);
			TextView originAddressText =  (TextView)contentView.findViewById(R.id.goods_origin_address);
			TextView endAddressText =  (TextView)contentView.findViewById(R.id.goods_end_address);
			TextView demandInfoText =  (TextView)contentView.findViewById(R.id.goods_demand_info);
			TextView noteInfoText =  (TextView)contentView.findViewById(R.id.goods_note_info);
			TextView goodsTypeText =  (TextView)contentView.findViewById(R.id.goods_type_2);
			if(data.get("i_goods_weight_type").equals("0")){
				goodsTypeText.setText(MyManage.getStringResources(getContext(),R.string.goods_type_0));
			}else
			{
				goodsTypeText.setText(MyManage.getStringResources(getContext(),R.string.goods_type_1));
			}
			costText.setText(vCost);
			shipmentTimerText.setText(vShipmentTimer);
			originAddressText.setText(vStartAddress);
			endAddressText.setText(vEndAddress);
			demandInfoText.setText(vDemandInfo);
			noteInfoText.setText(vNoteInfo);
			String telString = data.get("s_goods_phone");
			Button callPhone = (Button) contentView.findViewById(R.id.call_phone);
			callPhone.setTag(telString);
			callPhone.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg0) {
					String tel = (String) arg0.getTag();
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
					startActivity(intent);
				}
			});
		}
	}
}
