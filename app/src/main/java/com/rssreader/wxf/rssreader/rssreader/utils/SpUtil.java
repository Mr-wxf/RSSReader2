package com.rssreader.wxf.rssreader.rssreader.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.rssreader.wxf.rssreader.rssreader.constant.Value;

/**
 * Created by Administrator on 2016/12/21.
 */
public class SpUtil {

    public static void putBoolean(Context context, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("booleanValue", Context.MODE_APPEND);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(Value.isFirstUseApp, value);
        edit.commit();
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("booleanValue", Context.MODE_APPEND);
        boolean result = sp.getBoolean(key, false);
        return result;
    }
}
