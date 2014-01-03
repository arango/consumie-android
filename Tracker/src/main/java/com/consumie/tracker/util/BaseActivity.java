package com.consumie.tracker.util;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by Arango on 1/2/14.
 */
public class BaseActivity extends ActionBarActivity {
    public static API api = null;

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

    private void init_api() {
        if (api == null)
            api = new API(this);
        else
            api.setContext(this);
    }
}
