package com.example.jack.awesomebing;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jack.awesomebing.adapters.MyPagerAdapter;
import com.example.jack.awesomebing.beanForGson.PageInfo;
import com.example.jack.awesomebing.fragments.FavFragment;
import com.example.jack.awesomebing.fragments.IndexFragment;
import com.example.jack.awesomebing.fragments.SettingFragment;
import com.example.jack.awesomebing.retroService.BingService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewpager_content;
    private TabLayout tabLayout;

    public Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://bing.ioliu.cn/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        viewpager_content = (ViewPager) findViewById(R.id.viewpager_content);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        //初始化fragments和标题
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        IndexFragment indexFragment = new IndexFragment();
        FavFragment favFragment = new FavFragment();
        SettingFragment settingFragment = new SettingFragment();
        fragments.add(indexFragment);
        fragments.add(favFragment);
        fragments.add(settingFragment);

        String[] titles = {"首页","收藏","设置"};

        viewpager_content.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),fragments,titles));
        tabLayout.setupWithViewPager(viewpager_content);

        tabLayout.getTabAt(0).setCustomView(R.layout.bottom_item);
        tabLayout.getTabAt(1).setCustomView(R.layout.bottom_item);
        tabLayout.getTabAt(2).setCustomView(R.layout.bottom_item);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View bottomItem = tab.getCustomView();
                CheckBox icon = (CheckBox) bottomItem.findViewById(R.id.icon);
                TextView text = (TextView) bottomItem.findViewById(R.id.text);
                icon.setChecked(true);
                text.setTextColor(Color.parseColor("#ff0000"));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View bottomItem = tab.getCustomView();
                CheckBox icon = (CheckBox) bottomItem.findViewById(R.id.icon);
                TextView text = (TextView) bottomItem.findViewById(R.id.text);
                text.setText("changed");
                icon.setChecked(false);
                text.setTextColor(Color.parseColor("#555555"));

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




    }
}
