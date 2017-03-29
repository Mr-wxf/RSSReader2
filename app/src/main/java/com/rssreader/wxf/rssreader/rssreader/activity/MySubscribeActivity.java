package com.rssreader.wxf.rssreader.rssreader.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.rssreader.wxf.rssreader.R;
import com.rssreader.wxf.rssreader.rssreader.adapter.MySubscribeListViewAdapter;
import com.rssreader.wxf.rssreader.rssreader.bean.SubscribeChannels;
import com.rssreader.wxf.rssreader.rssreader.database.SubscribeChannelDAO;

import java.util.ArrayList;

public class MySubscribeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton ib_back_subscribe;
    private Context mContext;
    private SubscribeChannelDAO subscribeChannelDAO;
    private ArrayList<SubscribeChannels> subscribeChannelseList;
    private ListView lv_mysubscribe;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mySubscribeListViewAdapter = new MySubscribeListViewAdapter(subscribeChannelseList, getApplicationContext());
            lv_mysubscribe.setAdapter(mySubscribeListViewAdapter);
        }
    };
    private MySubscribeListViewAdapter mySubscribeListViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subscribe);
        this.mContext = this;
        initUI();
        loadData();
        lv_mysubscribe.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            private TextView tv_channel_name;
            private ImageButton ib_delete;

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                ib_delete = (ImageButton) view.findViewById(R.id.ib_delete);
                tv_channel_name = (TextView) view.findViewById(R.id.tv_channel_name);
                TranslateAnimation translateAnimation = new TranslateAnimation(
                        //X轴初始位置
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        //X轴移动的结束位置
                        Animation.RELATIVE_TO_SELF, 0.15f,
                        //y轴开始位置
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        //y轴移动后的结束位置
                        Animation.RELATIVE_TO_SELF, 0.0f);
                translateAnimation.setDuration(500);
                translateAnimation.setFillAfter(true);
                tv_channel_name.setAnimation(translateAnimation);
                ib_delete.setVisibility(View.VISIBLE);
                ib_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MySubscribeActivity.this);
                        builder.setMessage("是否删除该订阅");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                subscribeChannelDAO.delSubscribeChannel(subscribeChannelseList.get(position).getSubscribeChannelNames());
                                ib_delete.setVisibility(View.GONE);
                                TranslateAnimation translateAnimation = new TranslateAnimation(
                                        //X轴初始位置
                                        Animation.RELATIVE_TO_SELF, 0.15f,
                                        //X轴移动的结束位置
                                        Animation.RELATIVE_TO_SELF, 0.0f,
                                        //y轴开始位置
                                        Animation.RELATIVE_TO_SELF, 0.0f,
                                        //y轴移动后的结束位置
                                        Animation.RELATIVE_TO_SELF, 0.0f);
                                translateAnimation.setDuration(500);
                                translateAnimation.setFillAfter(true);
                                tv_channel_name.setAnimation(translateAnimation);
                                translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        subscribeChannelseList.remove(position);
                                        mySubscribeListViewAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {
                                    }
                                });

                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ib_delete.setVisibility(View.GONE);
                                TranslateAnimation translateAnimation = new TranslateAnimation(
                                        //X轴初始位置
                                        Animation.RELATIVE_TO_SELF, 0.15f,
                                        //X轴移动的结束位置
                                        Animation.RELATIVE_TO_SELF, 0.0f,
                                        //y轴开始位置
                                        Animation.RELATIVE_TO_SELF, 0.0f,
                                        //y轴移动后的结束位置
                                        Animation.RELATIVE_TO_SELF, 0.0f);
                                translateAnimation.setDuration(500);
                                translateAnimation.setFillAfter(true);
                                tv_channel_name.setAnimation(translateAnimation);
                            }
                        });
                        builder.show();
                    }
                });
                return false;
            }
        });
    }


    private void initUI() {
        ib_back_subscribe = (ImageButton) findViewById(R.id.ib_back_subscribe);
        lv_mysubscribe = (ListView) findViewById(R.id.lv_mysubscribe);
        ib_back_subscribe.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back_subscribe:
                startActivity(new Intent(mContext, MainActivity.class));
                finish();
                break;
        }
    }

    private void loadData() {
        subscribeChannelDAO = new SubscribeChannelDAO(getApplicationContext());
        new Thread() {
            @Override
            public void run() {
                subscribeChannelseList = subscribeChannelDAO.queryAllCollection();
                handler.sendEmptyMessage(0);
            }
        }.start();


    }
}
