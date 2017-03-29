package com.rssreader.wxf.rssreader.rssreader.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.rssreader.wxf.rssreader.R;
import com.rssreader.wxf.rssreader.rssreader.fragment.CollectionFragment;
import com.rssreader.wxf.rssreader.rssreader.fragment.FindFragment;
import com.rssreader.wxf.rssreader.rssreader.fragment.SubscribeFragment;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private RadioButton rb_subscribe;
    private ArrayList<Fragment> fragmentList;
    private FrameLayout fl;
    private RadioButton rb_collection;
    private RadioButton rb_find;
    private int mCurrentFragment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mContext = this;
        initUI();
        rb_subscribe.setOnClickListener(this);
        rb_collection.setOnClickListener(this);
        rb_find.setOnClickListener(this);
    }


    private void initUI() {
        rb_subscribe = (RadioButton) findViewById(R.id.rb_subscribe);
        fl = (FrameLayout) findViewById(R.id.fl);
        rb_collection = (RadioButton) findViewById(R.id.rb_collection);
        rb_find = (RadioButton) findViewById(R.id.rb_find);
        FragmentTransaction subscribeTransaction = getSupportFragmentManager().beginTransaction();
        subscribeTransaction.replace(R.id.fl, new SubscribeFragment());
        subscribeTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rb_subscribe:
                if (mCurrentFragment == 0) {
                    return;
                }
                FragmentTransaction subscribeTransaction = getSupportFragmentManager().beginTransaction();
                subscribeTransaction.replace(R.id.fl, new SubscribeFragment());
                subscribeTransaction.commit();
                mCurrentFragment = 0;
                break;
            case R.id.rb_collection:
                if (mCurrentFragment == 1) {
                    return;
                }
                FragmentTransaction collectionTransaction = getSupportFragmentManager().beginTransaction();
                collectionTransaction.replace(R.id.fl, new CollectionFragment());
                collectionTransaction.commit();
                mCurrentFragment = 1;
                break;
            case R.id.rb_find:
                if (mCurrentFragment == 2) {
                    return;
                }
                FragmentTransaction findTransaction = getSupportFragmentManager().beginTransaction();
                FindFragment findFragment = new FindFragment();
                findTransaction.replace(R.id.fl, findFragment);
                findTransaction.commit();
                mCurrentFragment = 2;
                break;
        }
    }

    /*
    * 程序结束后清除缓存
    * */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearWebViewCache();
    }

    /**
     * 清除WebView缓存
     */
    public void clearWebViewCache() {

        //清理Webview缓存数据库
        try {
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //WebView 缓存文件
        File appCacheDir = new File(getFilesDir().getAbsolutePath() + "/webcache");
        Log.e("ondestory", "appCacheDir path=" + appCacheDir.getAbsolutePath());

        File webviewCacheDir = new File(getCacheDir().getAbsolutePath() + "/webviewCache");
        Log.e("ondestory", "webviewCacheDir path=" + webviewCacheDir.getAbsolutePath());

        //删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        }
    }

}
