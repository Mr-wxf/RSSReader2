package com.rssreader.wxf.rssreader.rssreader.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rssreader.wxf.rssreader.R;
import com.rssreader.wxf.rssreader.rssreader.activity.WebViewActivity;
import com.rssreader.wxf.rssreader.rssreader.bean.News;

import java.util.ArrayList;

/**
 * Created by wxf on 2017/3/29.
 */
public class SubscribeChannelAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<News> newsList;

    public SubscribeChannelAdapter(Context context, ArrayList<News> newsList) {
        this.mContext = context;
        this.newsList = newsList;

    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.subscribe_channel_item, null);
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.ll_root = (LinearLayout) convertView.findViewById(R.id.ll_root);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String description = newsList.get(position).getDescription();
        String substring = "";
        if (description.contains("a")) {
            substring = description.trim().substring(description.indexOf("htm>") + 4, description.length() - 4);
        }
        if (description.contains("img")) {
            String content = description.trim().substring(description.indexOf("<p>") + 2, description.length() - description.length() / 3);
            substring = content.replaceAll("</p>", " ").replaceAll("<p>", "");
        }
        holder.tv_title.setText(newsList.get(position).getTitle());
        holder.tv_content.setText(substring);
        holder.ll_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = newsList.get(position).getLink();
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", url);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder {
        TextView tv_title;
        TextView tv_content;
        LinearLayout ll_root;
    }
}
