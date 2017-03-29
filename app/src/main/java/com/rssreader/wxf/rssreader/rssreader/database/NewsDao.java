package com.rssreader.wxf.rssreader.rssreader.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rssreader.wxf.rssreader.rssreader.bean.News;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/27.
 */
public class NewsDao {

    private final MySqliteOpenHelper mySqliteOpenHelper;
    private ArrayList<String> stringArrayList;
    private ArrayList<News> newsCollectionList;

    public NewsDao(Context context) {
        mySqliteOpenHelper = new MySqliteOpenHelper(context);
    }

    public void addHistoryRecord(String url) {
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        db.execSQL("insert into historyRecord(url) values(?)", new Object[]{url});
        db.close();
    }

    public void delHistoryRecord(String url) {
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        db.execSQL("delete from historyRecord where url=?", new Object[]{url});
        db.close();
    }

    public ArrayList<String> queryAllHistoryRecord() {
        stringArrayList = new ArrayList<>();
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select distinct url from historyRecord", null);
        if (null != cursor && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String string = cursor.getString(0);
                stringArrayList.add(string);
            }
            cursor.close();
        }
        db.close();
        return stringArrayList;
    }

    public void delAllHistoryRecord() {
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        db.execSQL("delete from historyRecord");
        db.close();
    }

    public void addCollection(String title, String description, String link, String date) {
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        db.execSQL("insert into collection(title,description,link,date) values(?,?,?,?)", new Object[]{title, description, link, date});
        db.close();
    }

    public void delCollection(String link) {
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        db.execSQL("delete from collection where link=?", new Object[]{link});
        db.close();
    }

    public ArrayList<News> queryAllCollection() {
        newsCollectionList = new ArrayList<>();
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select title,description,link,date from collection", null);
        if (null != cursor && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                News news = new News();
                String title = cursor.getString(0);
                news.setTitle(title);
                String description = cursor.getString(1);
                news.setDescription(description);
                String link = cursor.getString(2);
                String date = cursor.getString(3);
                news.setDate(date);
                news.setLink(link);
                newsCollectionList.add(news);
            }
            cursor.close();
        }
        db.close();
        return newsCollectionList;
    }
}
