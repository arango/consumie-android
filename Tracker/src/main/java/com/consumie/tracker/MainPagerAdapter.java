package com.consumie.tracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.consumie.tracker.util.API;
import com.consumie.tracker.util.BaseActivity;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Arango on 1/2/14.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private BaseActivity activity;
    private Integer cnt = 0;
    private ArrayList<Fragment> views = new ArrayList<Fragment>();

    public MainPagerAdapter(FragmentManager fm, BaseActivity act) {
        super(fm);
        this.activity = act;
    }

    @Override
    public int getCount() {
        return activity.getItems();
    }

    @Override
    public Fragment getItem(int position) {
        ContentFragment frag = new ContentFragment(activity.getItem(position), activity);
        return frag;
    }

}
