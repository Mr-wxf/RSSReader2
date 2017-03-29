package com.rssreader.wxf.rssreader.rssreader.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rssreader.wxf.rssreader.R;
import com.rssreader.wxf.rssreader.rssreader.adapter.CollcetionListViewAdapter;
import com.rssreader.wxf.rssreader.rssreader.bean.News;
import com.rssreader.wxf.rssreader.rssreader.database.NewsDao;

import java.util.ArrayList;


public class CollectionFragment extends Fragment {

    private ListView lv_collection;
    private NewsDao newsDao;
    private ArrayList<News> newsCollectionList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsDao = new NewsDao(getContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, null);
        newsCollectionList = newsDao.queryAllCollection();
        lv_collection = (ListView) view.findViewById(R.id.lv_collection);
        CollcetionListViewAdapter adapter = new CollcetionListViewAdapter(getContext(), newsCollectionList);
        lv_collection.setAdapter(adapter);
        return view;
    }


}
