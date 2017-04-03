package com.rssreader.wxf.rssreader.rssreader.activity;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by wxf on 2017/3/30.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
