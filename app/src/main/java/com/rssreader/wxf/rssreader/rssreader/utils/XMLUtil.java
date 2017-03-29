package com.rssreader.wxf.rssreader.rssreader.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/21.
 */
public class XMLUtil {
    private Context mContext;
    private ArrayList<Object> list;

    public XMLUtil(Context context) {
        this.mContext = context;
    }

    public InputStream send(String url) {
        InputStream in = null;
        try {
            URL Url = new URL(url);
            HttpURLConnection http = (HttpURLConnection) Url.openConnection();
            in = http.getInputStream();
            return in;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return in;
    }

    public ArrayList<Object> getData(InputStream in, String encode, final Class<?> clazz,
                                     final List<String> fields, final List<String> elements, final String itemElement) {
         list = new ArrayList<>();
        XmlPullParser response = Xml.newPullParser();
        try {
            response.setInput(in, encode);
            int eventType = response.getEventType();
            Object obj = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (itemElement.equals(response.getName())) {
                            obj = clazz.newInstance();
                        }
                        if (obj != null
                                && elements.contains(response.getName())) {
                            setFieldValue(obj, fields.get(elements
                                            .indexOf(response.getName())),
                                    response.nextText());

                        }
                        break;
                    case XmlPullParser.END_TAG:
                        response.getName();
                        if (itemElement.equals(response.getName())) {
                            this.list.add(obj);
                            obj = null;
                        }
                        break;
                }
                eventType = response.next();
            }
            return this.list;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Fragment.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        return this.list;
    }

    public void setFieldValue(Object obj, String propertyName,
                              Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

}

