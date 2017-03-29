package com.rssreader.wxf.rssreader.rssreader.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.rssreader.wxf.rssreader.R;
import com.rssreader.wxf.rssreader.rssreader.activity.WebViewActivity;
import com.rssreader.wxf.rssreader.rssreader.bean.News;
import com.rssreader.wxf.rssreader.rssreader.cache.LruImageCache;
import com.rssreader.wxf.rssreader.rssreader.database.NewsDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/22.
 */
public class FindListViewAdapter extends BaseAdapter {
    private ArrayList<News> newsList;
    private Context mContext;
    private final NewsDao newsDao;
    private ArrayList<News> newsCollectionList;
    private boolean[] state;//convertView复用状态位如果已收藏为true 未收藏为false
    private RequestQueue mQueue;

    public FindListViewAdapter(ArrayList<News> newsList, ArrayList<News> newsCollectionList, Context mContext) {
        this.newsList = newsList;
        this.mContext = mContext;
        this.newsCollectionList = newsCollectionList;
        newsDao = new NewsDao(mContext);
        mQueue = Volley.newRequestQueue(mContext);
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
        state = new boolean[newsList.size()];
        for (int i = 0; i < newsList.size(); i++) {
            state[i] = false;
        }
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.find_listview_item, null);
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.bt_collect = (Button) convertView.findViewById(R.id.bt_collect);
            holder.sml = (SwipeMenuLayout) convertView.findViewById(R.id.sml);
            holder.imageView = (NetworkImageView) convertView.findViewById(R.id.niv_imageview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.bt_collect.setTag(state[position]);
        holder.tv_title.setText(newsList.get(position).getTitle());
        String substring = "";
        String imagerUrl = "";
        String description = newsList.get(position).getDescription();
        if (description.contains("a") && !description.contains("img")) {
            substring = description.trim().substring(description.indexOf("htm>") + 4, description.length() - 4);
        }
        if (description.contains("img")) {
            imagerUrl = description.trim().substring(description.indexOf("src=") + 4, description.indexOf(".jpg") + 3);
            String content = description.trim().substring(description.indexOf("<p>") + 2, description.length() - description.length() / 3);
            substring = content.replaceAll("</p>", " ").replaceAll("<p>", "");
        }
        holder.imageUrl = imagerUrl;
        holder.tv_content.setText(substring);
        holder.tv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = newsList.get(position).getLink();
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", url);
                mContext.startActivity(intent);
            }
        });
        for (int i = 0; i < newsCollectionList.size(); i++) {
            if (newsList.get(position).getTitle().equals(newsCollectionList.get(i).getTitle())) {
                state[position] = true;
                holder.bt_collect.setTag(state[position]);
            }
        }
        if ((boolean) holder.bt_collect.getTag()) {
            holder.bt_collect.setText("已收藏");
            holder.bt_collect.setBackgroundColor(0xffcccccc);
        }
        if (!(boolean) holder.bt_collect.getTag()) {
            holder.bt_collect.setText("收藏");
            holder.bt_collect.setBackgroundColor(0xffff0000);
        }


        LruImageCache lruImageCache = LruImageCache.instance();

        ImageLoader imageLoader = new ImageLoader(mQueue, lruImageCache);
        if (imagerUrl.isEmpty()) {
            holder.imageView.setVisibility(View.GONE);
        }
        holder.imageView.setDefaultImageResId(R.drawable.default_image);
//        holder.imageView.setErrorImageResId(R.drawable.default_image);
        holder.imageView.setImageUrl(imagerUrl,
                imageLoader);
        holder.bt_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("已收藏".equals(holder.bt_collect.getText())) {
                    holder.sml.smoothClose();
                    return;
                }
                String title = newsList.get(position).getTitle();
                String description2 = newsList.get(position).getDescription();
                String link = newsList.get(position).getLink();
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = sDateFormat.format(new java.util.Date());
                newsDao.addCollection(title, description2, link, date);
                holder.bt_collect.setText("已收藏");
                state[position] = true;
                holder.bt_collect.setBackgroundColor(0xffcccccc);
                holder.sml.smoothClose();
            }
        });
        return convertView;
    }


    class ViewHolder {
        TextView tv_title;
        TextView tv_content;
        Button bt_collect;
        SwipeMenuLayout sml;
        String imageUrl;
        NetworkImageView imageView;
    }
    public void setNewsCollectionList(ArrayList<News> newsCollectionList){
        this.newsCollectionList=newsCollectionList;
    }
}

