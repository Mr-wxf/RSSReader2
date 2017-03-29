package com.rssreader.wxf.rssreader.rssreader.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.rssreader.wxf.rssreader.R;
import com.rssreader.wxf.rssreader.rssreader.activity.WebViewActivity;
import com.rssreader.wxf.rssreader.rssreader.bean.News;
import com.rssreader.wxf.rssreader.rssreader.database.NewsDao;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/29.
 */
public class CollcetionListViewAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<News> newsList;
    private final NewsDao newsDao;
    private boolean[] state;//

    public CollcetionListViewAdapter(Context context, ArrayList<News> newsList) {
        this.mContext = context;
        this.newsList = newsList;
        newsDao = new NewsDao(mContext);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.collection_listview_item, null);
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.bt_delete_collection = (Button) convertView.findViewById(R.id.bt_delete_collection);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.sml = (SwipeMenuLayout) convertView.findViewById(R.id.sml);
            holder.ll_root = (LinearLayout) convertView.findViewById(R.id.ll_root);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.bt_delete_collection.setTag(state[position]);
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
        holder.tv_date.setText(newsList.get(position).getDate());
        holder.bt_delete_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.sml.smoothClose();
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("提示");
                builder.setMessage("是否取消收藏？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newsDao.delCollection(newsList.get(position).getLink());
                        newsList.remove(position);
                        notifyDataSetChanged();
                        dialog.dismiss();
                        Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();

            }
        });
        holder.ll_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = newsList.get(position).getLink();
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", url);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_title;
        TextView tv_content;
        Button bt_delete_collection;
        TextView tv_date;
        SwipeMenuLayout sml;
        LinearLayout ll_root;
    }
}

