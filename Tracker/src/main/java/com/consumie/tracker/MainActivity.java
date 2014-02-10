package com.consumie.tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.consumie.tracker.models.ConsumptionOptions;
import com.consumie.tracker.models.Content;
import com.consumie.tracker.models.ContentList;
import com.consumie.tracker.models.Results;
import com.consumie.tracker.util.API;
import com.consumie.tracker.util.APICallback;
import com.consumie.tracker.util.BaseActivity;
import com.consumie.tracker.util.ConsumieViewPager;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends BaseActivity {

    ViewPager mPager;
    private ConsumptionOptions opts;
    private Integer windowHeight;
    private Integer windowWidth;
    private MainPagerAdapter fragmentPagerAdapter;
    private Boolean gettingContent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPager = (ViewPager)findViewById(R.id.pager);


        opts = new ConsumptionOptions();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        windowWidth = display.getWidth();
        windowHeight = display.getHeight();
        TimeZone tz = TimeZone.getDefault();
        opts.uimg = "80,80";
        opts.img = windowWidth + "," + windowHeight;
        opts.timezone = tz.getRawOffset() / 1000 / -60;
        opts.source = "friends";
        contents = new ArrayList<Content>();

        FragmentManager fm = getSupportFragmentManager();
        fragmentPagerAdapter = new MainPagerAdapter(fm, MainActivity.this);
        mPager.setAdapter(fragmentPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                if (i == 0 && v == 0 && i2 == 0) {
                    // at beginning - grab new content
                }
            }

            @Override
            public void onPageSelected(int i) {
                if (i >= contents.size() - 2)
                    getContent();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        getContent();
    }
    public void getContent() {
        if (gettingContent)
            return;
        gettingContent = true;
        api.Consumption(opts, new APICallback(this) {
            @Override
            public void success(Results results, Response response) {
                super.success(results, response);
                ContentList data = results.ContentList;
                opts.start_point = data.nextID;
                if (data.results != null) {
                    for (Content item : data.results) {
                        contents.add(item);
                    }
                }
                fragmentPagerAdapter.notifyDataSetChanged();
                gettingContent = false;
                if (contents.size() < 2)
                    getContent();
            }
            @Override
            public void failure(RetrofitError err) {
                super.failure(err);
                gettingContent = false;
            }
        });

    }



}
