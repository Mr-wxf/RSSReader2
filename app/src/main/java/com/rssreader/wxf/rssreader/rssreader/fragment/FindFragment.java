package com.rssreader.wxf.rssreader.rssreader.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.support.v7.widget.ListPopupWindow;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rssreader.wxf.rssreader.R;
import com.rssreader.wxf.rssreader.rssreader.adapter.ListPopupWindowAdapter;
import com.rssreader.wxf.rssreader.rssreader.adapter.FindListViewAdapter;
import com.rssreader.wxf.rssreader.rssreader.bean.News;
import com.rssreader.wxf.rssreader.rssreader.database.NewsDao;
import com.rssreader.wxf.rssreader.rssreader.utils.XMLUtil;
import com.rssreader.wxf.rssreader.rssreader.view.RefreshListView;
import com.rssreader.wxf.rssreader.rssreader.zxing.activity.CaptureActivity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class FindFragment extends Fragment implements View.OnClickListener, RefreshListView.OnRefreshListener {

    private RefreshListView lv_find_news;
    private EditText et_find_item;
    private ImageButton ib_clear_item;
    private ImageButton ib_find_item;
    private ArrayList<News> newsList;
    private static int start = 0;
    private FindListViewAdapter findListViewAdapter;
    private ArrayList<News> nowNewsList;
    private ProgressBar pb_loading;
    private XMLUtil xmlUtil;
    private ImageButton ib_qRcode;
    private ListPopupWindow mListPopupWindow;
    private NewsDao newsDao;
    private ListPopupWindowAdapter listPopupWindowAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    loadDataAndRefresh();
                    pb_loading.setVisibility(View.GONE);
                    break;
                case 1:
                    stringArrayList = (ArrayList<String>) msg.obj;
                    listPopupWindowAdapter = new ListPopupWindowAdapter(stringArrayList, getContext());
                    mListPopupWindow = new ListPopupWindow(getActivity());
                    listPopupWindowAdapter.notifyDataSetChanged();
                    mListPopupWindow.setAdapter(listPopupWindowAdapter);
                    mListPopupWindow.setWidth(ActionBarOverlayLayout.LayoutParams.WRAP_CONTENT);
                    mListPopupWindow.setHeight(ActionBarOverlayLayout.LayoutParams.WRAP_CONTENT);
                    mListPopupWindow.setAnchorView(et_find_item);//设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
                    mListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            et_find_item.setText(stringArrayList.get(position));
                            mListPopupWindow.dismiss();
                        }
                    });
                    mListPopupWindow.show();
                    ListView lv = mListPopupWindow.getListView();
                    TextView tv = new TextView(getContext());
                    tv.setTextSize(20);
                    tv.setText("清空历史记录");
                    tv.setGravity(Gravity.CENTER);
                    tv.setPadding(0, 20, 0, 20);
                    lv.addFooterView(tv);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            newsDao.delAllHistoryRecord();
                            stringArrayList.clear();
                            listPopupWindowAdapter.notifyDataSetChanged();
                        }
                    });
                    break;
            }


        }
    };
    private ArrayList<String> stringArrayList;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsDao = new NewsDao(getContext());
        nowNewsList = new ArrayList<>();
        newsList = new ArrayList<>();
        xmlUtil = new XMLUtil(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, null);
        lv_find_news = (RefreshListView) view.findViewById(R.id.lv_find_news);
        et_find_item = (EditText) view.findViewById(R.id.et_find_item);
        ib_clear_item = (ImageButton) view.findViewById(R.id.ib_clear_item);
        ib_find_item = (ImageButton) view.findViewById(R.id.ib_find_item);
        pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);
        ib_qRcode = (ImageButton) view.findViewById(R.id.ib_QRcode);
        ib_find_item.setOnClickListener(this);
        ib_clear_item.setOnClickListener(this);
        et_find_item.setOnClickListener(this);
        lv_find_news.setRefreshListener(this);
        ib_qRcode.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_find_item:
                if (et_find_item.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "请输入RSS", Toast.LENGTH_SHORT).show();
                    break;
                }
                newsDao.addHistoryRecord(et_find_item.getText().toString().trim());//添加历史记录数据库
                loadData();
                pb_loading.setVisibility(View.VISIBLE);
                break;
            case R.id.ib_clear_item:
                et_find_item.setText("");
                break;
            case R.id.ib_QRcode:
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.et_find_item:
                new Thread() {
                    @Override
                    public void run() {
                        ArrayList<String> stringArrayList = newsDao.queryAllHistoryRecord();
                        Message msg = new Message();
                        msg.obj = stringArrayList;
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                }.start();
                break;
        }
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
                newsList.clear();
                if (et_find_item.getText().toString().trim() != null && xmlUtil != null) {
                    InputStream in = xmlUtil.send(et_find_item.getText().toString().trim());
                    ArrayList<Object> objects = xmlUtil.getData(in, "UTF-8", News.class, fields, elements, "item");

                    for (int i = 0; i < objects.size(); i++) {
                        News news = (News) objects.get(i);
                        newsList.add(news);
                    }
                }
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
            }
        }.start();
    }

    private void loadDataAndRefresh() {
        start = 0;
        if (newsList != null && newsList.size() > 0) {
            nowNewsList.clear();
            for (int i = 0; i < 10; i++) {
                if (start >= newsList.size()) {
                    start = 0;
                }
                nowNewsList.add(newsList.get(start));
                start++;
            }
        }
        if (nowNewsList != null && nowNewsList.size() > 0) {
            ArrayList<News> newsCollectionList = newsDao.queryAllCollection();
            findListViewAdapter = new FindListViewAdapter(nowNewsList, newsCollectionList, getContext());

                findListViewAdapter.notifyDataSetChanged();
                lv_find_news.setOnRefresh();

            lv_find_news.setAdapter(findListViewAdapter);

        }
        lv_find_news.setOnRefresh();
    }

    //下拉刷新
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 1500);
    }

    //加载更多
    @Override
    public void LoadMore() {
        if (nowNewsList.size() >= newsList.size()) {
            ArrayList<News> newsCollectionList = newsDao.queryAllCollection();
            findListViewAdapter.setNewsCollectionList(newsCollectionList);
            findListViewAdapter.notifyDataSetChanged();
            lv_find_news.setOnRefresh();
            return;
        }
        handler.postDelayed(new Runnable() {
            ArrayList<News> newsCollectionList = newsDao.queryAllCollection();
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    if (start >= newsList.size() - 1) {
                        start = 0;
                    }
                    if (newsList != null && newsList.size() > 0) {
                        nowNewsList.add(newsList.get(start));
                        if (nowNewsList.size() >= newsList.size()) {

                            findListViewAdapter.setNewsCollectionList(newsCollectionList);
                            findListViewAdapter.notifyDataSetChanged();
                            lv_find_news.setOnRefresh();
                            return;
                        }
                        start++;
                    }
                    if (nowNewsList.size() >= newsList.size()) {
                        lv_find_news.setOnRefresh();
                        return;
                    }
                }

//                if (nowNewsList != null) {
//                    ArrayList<News> newsCollectionList = newsDao.queryAllCollection();
//                    findListViewAdapter = new FindListViewAdapter(nowNewsList, newsCollectionList, getContext());
//                    findListViewAdapter.notifyDataSetChanged();
//                    lv_find_news.setOnRefresh();
//                }
                ArrayList<News> newsCollectionList = newsDao.queryAllCollection();
                findListViewAdapter.setNewsCollectionList(newsCollectionList);
                findListViewAdapter.notifyDataSetChanged();
                lv_find_news.setOnRefresh();
            }

        }, 1500);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && data != null) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            et_find_item.setText(scanResult);
        }
    }
}
