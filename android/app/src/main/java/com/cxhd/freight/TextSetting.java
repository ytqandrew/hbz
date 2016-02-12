package com.cxhd.freight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Li on 2016/1/12.
 */
public class TextSetting extends Activity {
    private EditText editText;
    private int manageType = -1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_setting);
        Intent intent = getIntent();
        manageType = intent.getIntExtra("com.cxhd.freight.type",-1);
        editText = (EditText) findViewById(R.id.inputText);
        TextView titleText1 = (TextView) findViewById(R.id.setting_column_title);
        TextView titleText2 = (TextView) findViewById(R.id.setting_top_title);
        switch (manageType){
            case 0:
                titleText1.setText("QQ");
                titleText2.setText("QQ");
                break;
            case 1:
                titleText1.setText("所属公司");
                titleText2.setText("所属公司");
                break;
            case 2:
                titleText1.setText("随车电话");
                titleText2.setText("随车电话");
                break;
        }
        TextView backBtn = (TextView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                TextSetting.this.finish();
            }
        });
    }
    public void onClick_Event(View view){
        String msg = editText.getText().toString();
        int checkedId = view.getId();
        switch (checkedId) {
            case R.id.clearString:
                //清除输入框信息
                if(msg!=""){
                    editText.setText("");
                }
                break;
            case R.id.setting_enter:
                Toast.makeText(getApplicationContext(), "QQ号码不能为空", Toast.LENGTH_SHORT).show();
                break;
            default:
                Log.d("setting", "其它");
                //tabHost.setCurrentTabByTag("??????");
                break;
        }
    }
    protected  void onPause(){
        super.onPause();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }
}
