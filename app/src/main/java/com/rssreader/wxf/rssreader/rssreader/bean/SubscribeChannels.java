package com.rssreader.wxf.rssreader.rssreader.bean;

/**
 * Created by wxf on 2017/3/28.
 */
public class SubscribeChannels {
    private String subscribeChannelNames;
    private String subscribeChannelUrl;
    private String state;

    public String isState() {
        return state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setSubscribeChannelNames(String subscribeChannelNames) {
        this.subscribeChannelNames = subscribeChannelNames;
    }

    public void setSubscribeChannelUrl(String subscribeChannelUrl) {
        this.subscribeChannelUrl = subscribeChannelUrl;
    }

    public String getSubscribeChannelUrl() {
        return subscribeChannelUrl;
    }

    public String getSubscribeChannelNames() {
        return subscribeChannelNames;
    }
}
