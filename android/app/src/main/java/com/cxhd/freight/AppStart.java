package com.cxhd.freight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by LJ on 2015/10/26.
 */
public class AppStart extends Activity {
    private View view;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyManage.initJsonData(getBaseContext());
        view = View.inflate(this, R.layout.app_start, null);
        setContentView(view);
        checkNet();
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.icon_stub) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                        // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
        //Log.d("TAG","testMsg");
    }
    /*--
        检查网络连接
    --*/
    private void checkNet(){
        ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cwjManager.getActiveNetworkInfo();
        if(networkInfo!=null&&cwjManager.getActiveNetworkInfo().isAvailable()){
            // 渐变展示启动屏
            AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
            aa.setDuration(1000);
            view.startAnimation(aa);
            aa.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation arg0) {
                    redirectTo();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }
            });
        }else{
            //未连接网络
            TextView netError = (TextView)findViewById(R.id.netError);
            netError.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 跳转到...
     */
    private void redirectTo() {
        // 跳转进首页
        Intent mainAC = new Intent(this,HomeActivity.class);
        startActivity(mainAC);
        // 跳转进登陆
        //Intent mainAC = new Intent(this,Landing.class);
        //startActivity(mainAC);
        finish();
    }
}