package com.consumie.tracker.util;

import com.consumie.tracker.models.AddCommentOptions;
import com.consumie.tracker.models.ConsumptionOptions;
import com.consumie.tracker.models.LoginSubmission;
import com.consumie.tracker.models.Results;

import retrofit.RestAdapter;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by Arango on 1/2/14.
 */
public interface APIInterface {
    @Headers("User-Agent: Android")
    @PUT("/login/index.php")
    public void login(@Body LoginSubmission login, Callback<Results> callback);

    @Headers("User-Agent: Android")
    @PUT("/content/index.php")
    public void consumption(@Body ConsumptionOptions options, Callback<Results> callback);

    @Headers("User-Agent: Android")
    @PUT("/add_note/index.php")
    public void add_comment(@Body AddCommentOptions options, Callback<Results> callback);
}
