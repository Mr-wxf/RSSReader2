package com.rssreader.wxf.rssreader.rssreader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.rssreader.wxf.rssreader.R;
import com.rssreader.wxf.rssreader.rssreader.adapter.SubscribeChannelAdapter;
import com.rssreader.wxf.rssreader.rssreader.bean.News;
import com.rssreader.wxf.rssreader.rssreader.utils.XMLUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SubscribeChannelActivity extends AppCompatActivity {

    private TextView tv_subscribe_channel;
    private ListView lv_subscribe_channel;
    private XMLUtil xmlUtil;
    private String url;
    private ArrayList<News> newsList;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            SubscribeChannelAdapter subscribeChannelAdapter = new SubscribeChannelAdapter(getApplicationContext(), newsList);
            lv_subscribe_channel.setAdapter(subscribeChannelAdapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_channel);
        initUI();

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        url = intent.getStringExtra("url");
        tv_subscribe_channel.setText(name);
        loadData();
    }

    private void initUI() {
        tv_subscribe_channel = (TextView) findViewById(R.id.tv_subscribe_channel);
        lv_subscribe_channel = (ListView) findViewById(R.id.lv_subscribe_channel);
        xmlUtil = new XMLUtil(getApplicationContext());
        newsList = new ArrayList<>();

    }

    private void loadData() {
        final List<String> fields = new ArrayList<String>();
        fields.add("title");
        fields.add("link");
        fields.add("description");
        final List<String> elements = new ArrayList<String>();
        elements.add("title");
        elements.add("link");
        elements.add("description");
        new Thread() {
            @Override
            public void run() {
                InputStream in = xmlUtil.send(url);
                ArrayList<Object> objects = xmlUtil.getData(in, "UTF-8", News.class, fields, elements, "item");
                for (int i = 0; i < objects.size(); i++) {
                    News news = (News) objects.get(i);
                    newsList.add(news);
                }
                handler.sendEmptyMessage(0);
            }

        }.start();
    }
}
