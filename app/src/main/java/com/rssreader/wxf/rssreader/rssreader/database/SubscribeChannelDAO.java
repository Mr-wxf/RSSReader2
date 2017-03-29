package com.rssreader.wxf.rssreader.rssreader.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rssreader.wxf.rssreader.rssreader.bean.SubscribeChannels;

import java.util.ArrayList;

/**
 * Created by wxf on 2017/3/28.
 */
public class SubscribeChannelDAO {
    private final MySqliteOpenHelper mySqliteOpenHelper;
    private ArrayList<SubscribeChannels> subscribeChannelsList;

    public SubscribeChannelDAO(Context context) {
        mySqliteOpenHelper = new MySqliteOpenHelper(context);
    }

    public void addSubscribeChannel(String name,String url,String state) {
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        db.execSQL("insert into subscribechannel(name,url,state) values(?,?,?)", new Object[]{name,url,state});
        db.close();
    }

    public void delSubscribeChannel(String name) {
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        db.execSQL("delete from subscribechannel where name=?", new Object[]{name});
        db.close();
    }
    public void updateSubscribeChannel(String state,String name) {
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("state", state);
        db.update("subscribechannel",contentValues,"name=?",new String[]{name});
        db.close();
    }
    public ArrayList<SubscribeChannels> queryAllCollection() {
        subscribeChannelsList = new ArrayList<SubscribeChannels>();
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select name,url,state from subscribechannel group by name", null);
        if (null != cursor && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SubscribeChannels subscribeChannels = new SubscribeChannels();
                String name = cursor.getString(0);
                subscribeChannels.setSubscribeChannelNames(name);
                String url = cursor.getString(1);
                subscribeChannels.setSubscribeChannelUrl(url);
                String state = cursor.getString(2);
                subscribeChannels.setState(state);
                subscribeChannelsList.add(subscribeChannels);
            }
            cursor.close();
        }
        db.close();
        return subscribeChannelsList;
    }
    public String queryState(String name) {
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("subscribechannel", new String[]{"state"}, "name=?", new String[]{name}, null, null, null);
        String state="";
        if (null != cursor && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                 state = cursor.getString(0);
            }
            cursor.close();
        }
        db.close();
        return state;
    }
    public boolean isSubscribeChannelExist(){
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        boolean result=false;
        Cursor cursor = db.rawQuery("select name from subscribechannel group by name", null);
        if (null != cursor && cursor.getCount() > 0) {
             result=true;
            cursor.close();
            }
        db.close();
        return result;
        }


}
