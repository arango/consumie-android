package com.consumie.tracker.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;

import com.consumie.tracker.models.Content;

import java.util.List;

/**
 * Created by Arango on 1/2/14.
 */
public class BaseActivity extends ActionBarActivity {
    public static API api = null;
    public static List<Content> contents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_api();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }
    @Override
    protected void onResume() {
        super.onResume();
        init_api();
    }
    public Content getItem(int pos) {
        if (contents.size() > pos - 1) {
            return contents.get(pos);
        } else {
            return null;
        }
    }
    public int getItems() {
        return contents.size();
    }
    private void init_api() {
        if (api == null)
            api = new API(this);
        else
            api.setContext(this);
    }
}
