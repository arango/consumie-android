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
    private BaseActivity context;
    public Boolean showOverlay = true;
    public APICallback(BaseActivity context){
        this.context = context;
    }

    @Override
    public void success(Results results, Response response) {
        Log.d("READY", "Success");
        if (showOverlay)
            context.api.hideProgress();
    }
    @Override
    public void failure(RetrofitError retrofitError) {
        if (showOverlay)
            context.api.hideProgress();
        if (retrofitError == null) {
            String no_connection = this.context.getResources().getString(R.string.connectivity_error);
            Crouton.makeText(this.context, no_connection, Style.ALERT).show();
        } else {
            Crouton.makeText(this.context, this.context.getResources().getString(R.string.server_error), Style.ALERT).show();
        }
    }

}
