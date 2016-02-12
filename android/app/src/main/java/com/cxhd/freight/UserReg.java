package com.cxhd.freight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cxhd.freight.data.CallBackData;

/**
 * 用户注册组件
 * Created by Li on 2016/1/22.
 */
public class UserReg extends Activity implements CallBackData {
    public EditText phoneText;
    public EditText passwordText;
    public EditText codeText;
    public EditText usernameText;
    public EditText plateText;
    public Button codeBtn;
    public Button regBtn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_reg);
        phoneText = (EditText) findViewById(R.id.reg_phone);
        passwordText = (EditText) findViewById(R.id.reg_password);
        codeText = (EditText) findViewById(R.id.reg_code);
        usernameText = (EditText) findViewById(R.id.reg_user_name);
        plateText = (EditText) findViewById(R.id.reg_car_plate);
        codeBtn = (Button) findViewById(R.id.reg_code_btn);
        regBtn = (Button) findViewById(R.id.reg_btn);
    }
    public void onClick_Event(View view){
        int checkedId = view.getId();
        switch (checkedId) {
            case R.id.reg_back:
                //返回
                finish();
                break;
            case  R.id.reg_btn:
                //注册
                String phone1 = phoneText.getText().toString();
                String password = passwordText.getText().toString();
                String username = usernameText.getText().toString();
                String plate = plateText.getText().toString();
                if(password!=""&&username!=""&&plate!=""){
                    regBtn.setEnabled(false);
                    MyManage.sendRegRequest("1",phone1,"1234",password,username,plate,this);
                }else{
                    Toast.makeText(getApplicationContext(), MyManage.getStringResources(this,R.string.error_9), Toast.LENGTH_SHORT).show();
                }
                break;
            case  R.id.reg_code_btn:
                //获取验证码
                String phone = phoneText.getText().toString();
                if(MyManage.isMobileNO(phone)){
                    codeBtn.setEnabled(false);
                    MyManage.sendCodeRequest("1", phone, this);
                }else{
                    Toast.makeText(getApplicationContext(), MyManage.getStringResources(this,R.string.error_4), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    public void callBack(String mid,String state,String err_id,String msg){
        if(mid.equals("1")){
            codeCallBack(state, err_id, msg);
        }else{
            regCallBack(state, err_id, msg);
        }
    }
    private void codeCallBack(String state,String err_id,String msg){
        String err_str = "";
        if(state.equals("1")){
            codeText.setText("1234");
            codeText.setEnabled(false);
            phoneText.setEnabled(false);
            regBtn.setEnabled(true);
        }else{
            switch (err_id){
                case "1":
                    err_str = MyManage.getStringResources(this,R.string.error_5);
                    break;
            }
            Toast.makeText(getApplicationContext(), err_str, Toast.LENGTH_SHORT).show();
            codeBtn.setEnabled(true);
        }
    }
    private void regCallBack(String state,String err_id,String msg){
        String err_str = "";
        if(state.equals("1")){
            Log.d("REG","Session:"+msg);
            AppConfig.setSession(msg);
            finish();
            //Landing.instance.finish();
        }else{
            switch (err_id){
                case "1":
                    err_str = MyManage.getStringResources(this,R.string.error_6);
                    break;
                case "2":
                    err_str = MyManage.getStringResources(this,R.string.error_7);
                    break;
                case "3":
                    err_str = MyManage.getStringResources(this,R.string.error_8);
                    break;
            }
            Toast.makeText(getApplicationContext(), err_str, Toast.LENGTH_SHORT).show();
            regBtn.setEnabled(true);
        }
    }
    protected  void onPause(){
        super.onPause();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }
}
