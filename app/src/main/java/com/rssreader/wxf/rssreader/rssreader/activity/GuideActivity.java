package com.rssreader.wxf.rssreader.rssreader.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.rssreader.wxf.rssreader.R;
import com.rssreader.wxf.rssreader.rssreader.utils.SpUtil;
import com.rssreader.wxf.rssreader.rssreader.constant.Value;
import com.rssreader.wxf.rssreader.rssreader.adapter.ViewPagerAdapter;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {

    private ViewPager vp;
    private Context mContext;
    private ArrayList<ImageView> imageViewList;
    private Button bt_now_use;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        this.mContext = this;
        boolean result = SpUtil.getBoolean(mContext, Value.isFirstUseApp);
        if (result) {
            startActivity(new Intent(mContext, WelcomeActivity.class));
            finish();
        }
        initUI();
        initData();
        vp.setAdapter(new ViewPagerAdapter(imageViewList));
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == imageViewList.size() - 1) {
                    bt_now_use.setVisibility(View.VISIBLE);
                    bt_now_use.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(mContext, WelcomeActivity.class));
                            SpUtil.putBoolean(mContext, true);
                            finish();
                        }
                    });
                } else {
                    bt_now_use.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initUI() {
        vp = (ViewPager) findViewById(R.id.vp);
        bt_now_use = (Button) findViewById(R.id.bt_now_use);
    }

    private void initData() {
        imageViewList = new ArrayList<>();
        int[] ints = new int[]{R.drawable.guide_one, R.drawable.guide_two, R.drawable.guide_three};
        for (int i = 0; i < ints.length; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setBackgroundResource(ints[i]);
            imageViewList.add(imageView);
        }
    }
}
