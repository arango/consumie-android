package com.consumie.tracker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.consumie.tracker.util.API;
import com.consumie.tracker.util.BaseActivity;

import java.util.Locale;

/**
 * Created by Arango on 1/2/14.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
    private BaseActivity activity;
    public MainPagerAdapter(FragmentManager fm, BaseActivity activity) {
        super(fm);
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (position == 0) {
            ListFragment fragment1 = new ListFragment(this.activity);
            return fragment1;
        } else if (position == 1) {
            ListFragment fragment2 = new ListFragment(this.activity);
            return fragment2;
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }
}
