package com.cxhd.freight;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
* 我的设置组件
*/
public class SettingFragment extends Fragment {
	private View layoutView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
		layoutView = inflater.inflate(R.layout.setting, null);
		View.OnClickListener clickEvent = new View.OnClickListener(){
			public void onClick(View view){
				Intent intent = null;
				int checkedId = view.getId();
				switch (checkedId) {
					case R.id.set_user_info:
						//个人信息
						intent = new Intent(getActivity(),UserInfo.class);
						break;
					case  R.id.set_user_prove:
						//认证信息
						intent = new Intent(getActivity(),MyProve.class);
						break;
//					case  R.id.page_3:
//						pageBtn3.setSelected(true);
//						break;
				}
				if(intent!=null){
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
				}
			}
		};
		layoutView.findViewById(R.id.set_user_info).setOnClickListener(clickEvent);
		layoutView.findViewById(R.id.set_user_prove).setOnClickListener(clickEvent);
		return layoutView;
	}
}
