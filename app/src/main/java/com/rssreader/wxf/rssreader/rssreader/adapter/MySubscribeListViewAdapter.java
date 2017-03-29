package com.rssreader.wxf.rssreader.rssreader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rssreader.wxf.rssreader.R;
import com.rssreader.wxf.rssreader.rssreader.bean.SubscribeChannels;

import java.util.ArrayList;

/**
 * Created by wxf on 2017/3/29.
 */
public class MySubscribeListViewAdapter extends BaseAdapter {
    private  ArrayList<SubscribeChannels> subscribeChannelses;
    private Context mContext;
    public MySubscribeListViewAdapter(ArrayList<SubscribeChannels> subscribeChannelses,Context context){
        this.subscribeChannelses=subscribeChannelses;
        this.mContext=context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.mysubscribe_listview_item, null);
            holder.tv_channel_name = (TextView) convertView.findViewById(R.id.tv_channel_name);
            holder.bt_subscribe = (ImageButton) convertView.findViewById(R.id.ib_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ;
        holder.tv_channel_name.setText(subscribeChannelses.get(position).getSubscribeChannelNames());
        return convertView;
    }

    class ViewHolder {
        TextView tv_channel_name;
        ImageButton bt_subscribe;

    }
}
