package com.consumie.tracker.util;

import android.util.Log;

import com.consumie.tracker.R;
import com.consumie.tracker.models.Results;

import java.util.Date;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Arango on 1/2/14.
 */
public class APICallback implements Callback<Results> {
    private API api;
    public Boolean showOverlay = true;
    public APICallback(API api){
        this.api = api;
    }

    @Override
    public void success(Results results, Response response) {
        Log.d("READY", "Success");
        if (showOverlay)
            this.api.hideProgress();
    }
    @Override
    public void failure(RetrofitError retrofitError) {
        if (showOverlay)
            this.api.hideProgress();
        if (retrofitError == null) {
            this.api.showCrouton(R.string.connectivity_error);
        } else {
            this.api.showCrouton(R.string.server_error);
        }
    }

}
