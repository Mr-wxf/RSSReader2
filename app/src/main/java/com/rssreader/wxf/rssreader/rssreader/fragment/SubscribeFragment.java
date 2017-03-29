package com.rssreader.wxf.rssreader.rssreader.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.rssreader.wxf.rssreader.R;
import com.rssreader.wxf.rssreader.rssreader.activity.MySubscribeActivity;
import com.rssreader.wxf.rssreader.rssreader.activity.SubscribeChannelActivity;
import com.rssreader.wxf.rssreader.rssreader.adapter.SubscribeChannelListViewAdapter;
import com.rssreader.wxf.rssreader.rssreader.bean.SubscribeChannels;
import com.rssreader.wxf.rssreader.rssreader.database.SubscribeChannelDAO;

import java.util.ArrayList;


public class SubscribeFragment extends Fragment implements View.OnClickListener {

    private ImageButton ib_item_move;
    private ListView lv_subscribe_channel;
    private ArrayList<SubscribeChannels> subscribeChannelses;
    private SubscribeChannelListViewAdapter subscribeChannelListViewAdapter;
    private SubscribeChannelDAO subscribeChannelDAO;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            subscribeChannelListViewAdapter = new SubscribeChannelListViewAdapter(subscribeChannelses, getContext());
            lv_subscribe_channel.setAdapter(subscribeChannelListViewAdapter);
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadSubscribeChannel();
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscribe, null);
        ib_item_move = (ImageButton) view.findViewById(R.id.ib_item_move);
        lv_subscribe_channel = (ListView) view.findViewById(R.id.lv_subscribe_channel);
        ib_item_move.setOnClickListener(this);
        lv_subscribe_channel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), SubscribeChannelActivity.class);
                intent.putExtra("name", subscribeChannelses.get(position).getSubscribeChannelNames());
                intent.putExtra("url", subscribeChannelses.get(position).getSubscribeChannelUrl());
                startActivity(intent);
            }
        });
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_item_move:
                Intent intent = new Intent(getContext(), MySubscribeActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }
    }




    private void loadSubscribeChannel() {
        subscribeChannelses = new ArrayList<>();
        subscribeChannelDAO = new SubscribeChannelDAO(getContext());
        new Thread() {
            @Override
            public void run() {
                boolean result = subscribeChannelDAO.isSubscribeChannelExist();
                if (result) {
                    subscribeChannelses = subscribeChannelDAO.queryAllCollection();
                } else {
                    SubscribeChannels politicseChannel = new SubscribeChannels();
                    politicseChannel.setSubscribeChannelNames("时政频道");
                    politicseChannel.setSubscribeChannelUrl("http://www.xinhuanet.com/politics/news_politics.xml");
                    String politicseChannelState = subscribeChannelDAO.queryState("时政频道");
                    politicseChannel.setState(politicseChannelState);
                    subscribeChannelses.add(politicseChannel);
                    subscribeChannelDAO.addSubscribeChannel("时政频道", "http://www.xinhuanet.com/politics/news_politics.xml", politicseChannelState);

                    SubscribeChannels worldChannel = new SubscribeChannels();
                    worldChannel.setSubscribeChannelNames("国际频道");
                    worldChannel.setSubscribeChannelUrl("http://www.xinhuanet.com/world/news_world.xml");
                    String worldChannelState = subscribeChannelDAO.queryState("国际频道");
                    worldChannel.setState(worldChannelState);
                    subscribeChannelses.add(worldChannel);
                    subscribeChannelDAO.addSubscribeChannel("国际频道", "http://www.xinhuanet.com/world/news_world.xml", worldChannelState);

                    SubscribeChannels provinceChannel = new SubscribeChannels();
                    provinceChannel.setSubscribeChannelNames("地方频道");
                    provinceChannel.setSubscribeChannelUrl("http://www.xinhuanet.com/local/news_province.xml");
                    String provinceChannelState = subscribeChannelDAO.queryState("地方频道");
                    worldChannel.setState(provinceChannelState);
                    subscribeChannelses.add(provinceChannel);
                    subscribeChannelDAO.addSubscribeChannel("地方频道", "http://www.xinhuanet.com/local/news_province.xml", provinceChannelState);

                    SubscribeChannels milChannel = new SubscribeChannels();
                    milChannel.setSubscribeChannelNames("军事频道");
                    milChannel.setSubscribeChannelUrl("http://www.xinhuanet.com/mil/news_mil.xml");
                    String milChannellState = subscribeChannelDAO.queryState("军事频道");
                    worldChannel.setState(milChannellState);
                    subscribeChannelses.add(milChannel);
                    subscribeChannelDAO.addSubscribeChannel("军事频道", "http://www.xinhuanet.com/mil/news_mil.xml", milChannellState);

                    SubscribeChannels twChannel = new SubscribeChannels();
                    twChannel.setSubscribeChannelNames("台湾频道");
                    twChannel.setSubscribeChannelUrl("http://www.xinhuanet.com/tw/news_tw.xml");
                    String twChannelState = subscribeChannelDAO.queryState("台湾频道");
                    worldChannel.setState(twChannelState);
                    subscribeChannelses.add(twChannel);
                    subscribeChannelDAO.addSubscribeChannel("台湾频道", "http://www.xinhuanet.com/tw/news_tw.xml", twChannelState);

                    SubscribeChannels overseasChannel = new SubscribeChannels();
                    overseasChannel.setSubscribeChannelNames("华人频道");
                    overseasChannel.setSubscribeChannelUrl("http://www.xinhuanet.com/overseas/news_overseas.xml");
                    String overseasChannelState = subscribeChannelDAO.queryState("华人频道");
                    worldChannel.setState(overseasChannelState);
                    subscribeChannelses.add(overseasChannel);
                    subscribeChannelDAO.addSubscribeChannel("华人频道", "http://www.xinhuanet.com/overseas/news_overseas.xml", overseasChannelState);

                    SubscribeChannels financeChannel = new SubscribeChannels();
                    financeChannel.setSubscribeChannelNames("金融频道");
                    financeChannel.setSubscribeChannelUrl("http://www.xinhuanet.com/finance/news_finance.xml");
                    String financeChannelState = subscribeChannelDAO.queryState("金融频道");
                    worldChannel.setState(financeChannelState);
                    subscribeChannelses.add(financeChannel);
                    subscribeChannelDAO.addSubscribeChannel("金融频道", "http://www.xinhuanet.com/finance/news_finance.xml", financeChannelState);

                    SubscribeChannels fortuneChannel = new SubscribeChannels();
                    fortuneChannel.setSubscribeChannelNames("财经频道");
                    fortuneChannel.setSubscribeChannelUrl("http://www.xinhuanet.com/fortune/news_fortune.xml");
                    String fortuneChannelState = subscribeChannelDAO.queryState("财经频道");
                    worldChannel.setState(fortuneChannelState);
                    subscribeChannelses.add(fortuneChannel);
                    subscribeChannelDAO.addSubscribeChannel("财经频道", "http://www.xinhuanet.com/fortune/news_fortune.xml", fortuneChannelState);

                    SubscribeChannels sportsChannel = new SubscribeChannels();
                    sportsChannel.setSubscribeChannelNames("汽车频道");
                    sportsChannel.setSubscribeChannelUrl("http://www.xinhuanet.com/auto/news_auto.xml");
                    String sportsChannelState = subscribeChannelDAO.queryState("汽车频道");
                    worldChannel.setState(sportsChannelState);
                    subscribeChannelses.add(sportsChannel);
                    subscribeChannelDAO.addSubscribeChannel("汽车频道", "http://www.xinhuanet.com/sports/news_sports.xml", sportsChannelState);

                    SubscribeChannels entChannel = new SubscribeChannels();
                    entChannel.setSubscribeChannelNames("科技频道");
                    entChannel.setSubscribeChannelUrl("http://www.xinhuanet.com/tech/news_tech.xml");
                    String entChannelState = subscribeChannelDAO.queryState("科技频道");
                    worldChannel.setState(entChannelState);
                    subscribeChannelses.add(entChannel);
                    subscribeChannelDAO.addSubscribeChannel("科技频道", "http://www.xinhuanet.com/ent/news_ent.xml", entChannelState);


                    SubscribeChannels airChannel = new SubscribeChannels();
                    airChannel.setSubscribeChannelNames("民航频道");
                    airChannel.setSubscribeChannelUrl("http://www.xinhuanet.com/air/news_air.xml");
                    String airChannelState = subscribeChannelDAO.queryState("民航频道");
                    worldChannel.setState(airChannelState);
                    subscribeChannelses.add(airChannel);
                    subscribeChannelDAO.addSubscribeChannel("民航频道", "http://www.xinhuanet.com/air/news_air.xml", airChannelState);

                }
                handler.sendEmptyMessage(0);
            }
        }.start();


    }
}
