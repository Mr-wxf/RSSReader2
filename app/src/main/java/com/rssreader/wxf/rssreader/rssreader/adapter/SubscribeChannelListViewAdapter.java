package com.rssreader.wxf.rssreader.rssreader.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.rssreader.wxf.rssreader.R;
import com.rssreader.wxf.rssreader.rssreader.bean.SubscribeChannels;
import com.rssreader.wxf.rssreader.rssreader.database.SubscribeChannelDAO;

import java.util.ArrayList;

/**
 * Created by wxf on 2017/3/28.
 */
public class SubscribeChannelListViewAdapter extends BaseAdapter {
    private ArrayList<SubscribeChannels> subscribeChannelses;
    private Context mContext;
    private final SubscribeChannelDAO subscribeChannelDAO;

    public SubscribeChannelListViewAdapter(ArrayList<SubscribeChannels> subscribeChannelses, Context context) {
        this.subscribeChannelses = subscribeChannelses;
        this.mContext = context;
        subscribeChannelDAO = new SubscribeChannelDAO(mContext);
    }

    @Override
    public int getCount() {
        return subscribeChannelses.size();
    }

    @Override
    public Object getItem(int position) {
        return subscribeChannelses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.subscribe_channel_listview_item, null);
            holder.tv_politics_news = (TextView) convertView.findViewById(R.id.tv_politics_news);
            holder.bt_subscribe = (Button) convertView.findViewById(R.id.bt_subscribe);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ;
        holder.tv_politics_news.setText(subscribeChannelses.get(position).getSubscribeChannelNames());
        String state = subscribeChannelDAO.queryState(subscribeChannelses.get(position).getSubscribeChannelNames());
        if (state.contains("已")) {
            holder.bt_subscribe.setBackgroundColor(Color.GRAY);
            holder.bt_subscribe.setText("已订阅");
        } else {
            holder.bt_subscribe.setBackgroundColor(Color.RED);
            holder.bt_subscribe.setText("订阅");
        }
        holder.bt_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = (String) holder.bt_subscribe.getText().toString();
                if (text.contains("已")) {
                    subscribeChannelDAO.updateSubscribeChannel("订阅", subscribeChannelses.get(position).getSubscribeChannelNames());
                    holder.bt_subscribe.setText("订阅");
                    holder.bt_subscribe.setBackgroundColor(Color.RED);
                } else {
                    subscribeChannelDAO.updateSubscribeChannel("已订阅", subscribeChannelses.get(position).getSubscribeChannelNames());
                    holder.bt_subscribe.setText("已订阅");
                    holder.bt_subscribe.setBackgroundColor(Color.GRAY);
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_politics_news;
        Button bt_subscribe;

    }
}
