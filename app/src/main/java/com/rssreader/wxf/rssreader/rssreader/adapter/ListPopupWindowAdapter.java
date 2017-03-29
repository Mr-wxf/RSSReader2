package com.rssreader.wxf.rssreader.rssreader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.rssreader.wxf.rssreader.R;
import com.rssreader.wxf.rssreader.rssreader.database.NewsDao;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/27.
 */
public class ListPopupWindowAdapter extends BaseAdapter {
    private ArrayList<String> stringArrayList;
    private Context mContext;

    public ListPopupWindowAdapter(ArrayList<String> stringArrayList, Context context) {
        this.stringArrayList = stringArrayList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return stringArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return stringArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listpopuwindow_item, null);
            holder.tv_url = (TextView) convertView.findViewById(R.id.tv_url);
            holder.bt_del_history_record = (Button) convertView.findViewById(R.id.bt_del_history_record);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_url.setText(stringArrayList.get(position));
        holder.bt_del_history_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsDao newsDao = new NewsDao(mContext);
                newsDao.delHistoryRecord(stringArrayList.get(position));
                stringArrayList.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_url;
        Button bt_del_history_record;
    }
}
