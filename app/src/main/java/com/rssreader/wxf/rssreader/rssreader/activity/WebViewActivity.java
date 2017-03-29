package com.rssreader.wxf.rssreader.rssreader.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rssreader.wxf.rssreader.R;

public class WebViewActivity extends AppCompatActivity {

    private android.webkit.WebView wb;
    private ProgressBar pb_loading_web;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        this.mContext=this;
        wb = (android.webkit.WebView) findViewById(R.id.wb);
        pb_loading_web = (ProgressBar) findViewById(R.id.pb_loading_web);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        Log.d("url", url);
        WebSettings settings = wb.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        settings.setJavaScriptEnabled(true);
        //设置可以访问文件
        settings.setAllowFileAccess(true);
        //设置支持缩放
        settings.setBuiltInZoomControls(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //设置 缓存模式
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);
        //开启 database storage API 功能
        settings.setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + "/webcache";
        Log.i("TAG", "=" + cacheDirPath);
        //设置数据库缓存路径
        settings.setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        settings.setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        settings.setAppCacheEnabled(true);
        //加载需要显示的网页
        //设置Web视图
        wb.setWebViewClient(new WebViewClient() {
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webview, String url) {
                webview.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pb_loading_web.setVisibility(View.VISIBLE); // 显示加载界面
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pb_loading_web.setVisibility(View.GONE); // 隐藏加载界面
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Toast.makeText(mContext,"网络异常",Toast.LENGTH_SHORT).show();
                pb_loading_web.setVisibility(View.GONE); // 隐藏加载界面
            }
        });
        wb.loadUrl(url);
    }

}
