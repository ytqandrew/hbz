package com.cxhd.freight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cxhd.freight.data.CallBackData;


/**
 * 登陆组件
 * Created by Li on 2016/1/5.
 */
public class Landing extends Activity implements CallBackData {
    public static Activity instance;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.landing);
    }
    public void callBack(String mid,String state,String err_id,String msg){
        String err_str = "";
        if(state.equals("1")){
            AppConfig.setSession(msg);
            AppConfig.setLandingState(true);
            Intent mainAC = new Intent(this,MainActivity.class);
            startActivity(mainAC);
            finish();
        }else{
            switch (err_id){
                case "1":
                    err_str = MyManage.getStringResources(this,R.string.error_1);
                    break;
                case "2":
                    err_str = MyManage.getStringResources(this,R.string.error_1);
                    break;
                case "3":
                    err_str = MyManage.getStringResources(this,R.string.error_2);
                    break;
            }
            Toast.makeText(getApplicationContext(), err_str, Toast.LENGTH_SHORT).show();
        }
    }
    public void onClick_Event(View view){
        int checkedId = view.getId();
        if(checkedId==R.id.landingBtn){
            EditText userNameInput = (EditText) findViewById(R.id.userName);
            EditText userPassword = (EditText) findViewById(R.id.userPassword);
            String user_name = userNameInput.getText().toString();
            String user_password = userPassword.getText().toString();
            if(MyManage.isMobileNO(user_name)&&user_password!=""){
                MyManage.sendLoginRequest("1",user_name,user_password,this);
            }else{
                Toast.makeText(getApplicationContext(), MyManage.getStringResources(this,R.string.error_1), Toast.LENGTH_SHORT).show();
            }
        }else{
            Intent intent = null;
            switch (checkedId) {
                case R.id.landing_reg:
                    //注册
                    intent = new Intent(this,UserReg.class);
                    break;
                case  R.id.landing_modify_password:
                    //忘记密码
                    intent = new Intent(this,MyProve.class);
                    break;
            }
            if(intent!=null){
                startActivity(intent);
                Landing.this.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        }

    }
    protected  void onPause(){
        super.onPause();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }
}
