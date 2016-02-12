package com.cxhd.freight.custom;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Li on 2015/12/24.
 */
public class CustomFontTextView extends TextView {
    public CustomFontTextView(Context context){
        super(context);
        init(context);
    }
    public CustomFontTextView(Context context,AttributeSet attrs){
        super(context,attrs);
        init(context);
    }
    public CustomFontTextView(Context context,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
        init(context);
    }
    private void init(Context context){
        AssetManager assetManager = context.getAssets();
        Typeface font = Typeface.createFromAsset(assetManager,"icomoon.ttf");
        setTypeface(font);
    }
}
