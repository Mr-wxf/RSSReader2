package com.rssreader.wxf.rssreader.rssreader.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/12/27.
 */
public class MySqliteOpenHelper extends SQLiteOpenHelper {
    public MySqliteOpenHelper(Context context) {
        super(context, "RSS.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table historyRecord(_id integer primary key autoincrement,url vahchar(20))");//发现界面的搜索历史记录表
        db.execSQL("create table collection(_id integer primary key autoincrement,title vahchar(20),description vahchar(20),link vahchar(20),date vahchar(20))");
        db.execSQL("create table subscribechannel(_id integer primary key autoincrement,name vahchar(20),url vahchar(20),state vahchar(20))");//订阅数据表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
