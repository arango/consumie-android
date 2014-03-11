package com.consumie.tracker.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import com.consumie.tracker.R;
import com.consumie.tracker.models.AddCommentOptions;
import com.consumie.tracker.models.ConsumptionOptions;
import com.consumie.tracker.models.Login;
import com.consumie.tracker.models.LoginSubmission;
import com.google.gson.Gson;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import retrofit.RestAdapter;

/**
 * Created by Arango on 1/2/14.
 */
public class API {
    private static String API_URL = "http://www.consumie.com/api";
    private RestAdapter restAdapter;
    private APIInterface api;
    private Context c;
    private static SharedPreferences mSharedPreferences;
    private int loading;
    private ProgressDialog progressDialog;
    private Login user;
    private static Gson gson;
    private static final String PREF_LOGIN = "login_key";

    public API(BaseActivity context) {
        c = context;
        mSharedPreferences = this.c.getSharedPreferences("com.consumie.tracker", Context.MODE_PRIVATE);
        loading = 0;
        if (gson == null) {
            gson = new Gson();
        }
        restAdapter = new RestAdapter.Builder()
                .setServer(API_URL)
                .build();
        api = restAdapter.create(APIInterface.class);
    }
    public void setUser(Login l) {
        this.user = l;
        mSharedPreferences.edit().putString(PREF_LOGIN, gson.toJson(l)).commit();
    }
    public Login getSavedLogin() {
        String login_data = mSharedPreferences.getString(PREF_LOGIN, null);
        Login l = null;
        if (login_data != null) {
                l = gson.fromJson(login_data, Login.class);
        }
        if (l == null)
            return null;
        else
            return l;
    }
    public Login getUser() {
        return this.user;
    }
    public void setContext(BaseActivity context) {
        c = context;
        loading = 0;
    }
    public void showProgress() {
        if (loading == 0) {
            progressDialog = ProgressDialog.show(c, "", "Loading");
        }
        loading++;
    }

    public void hideProgress() {
        loading--;
        if (loading < 0)
            loading = 0;
        if (loading == 0 && progressDialog != null) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                loading = 0;
            }
        }
    }
    public void showCrouton(int msg) {
        String txt = this.c.getResources().getString(msg);
        Crouton.makeText((Activity)this.c, txt, Style.ALERT).show();
    }

    public void Consumption(ConsumptionOptions options, APICallback cb) {
        cb.showOverlay = false;
        if (isNetworkConnected()) {
            options.userKey = this.user.userkey;
            api.consumption(options, cb);
        } else {
            cb.failure(null);
        }
    }
    public void AddComment(AddCommentOptions options, APICallback cb) {
        if (isNetworkConnected()) {
            options.userKey = this.user.userkey;
            api.add_comment(options, cb);
        } else {
            cb.failure(null);
        }
    }
    public void Login(String username, String password, APICallback cb) {
        cb.showOverlay = false;
        if (isNetworkConnected()) {
            LoginSubmission submission = new LoginSubmission();
            submission.username = username;
            submission.password = "";
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(password.getBytes());
                byte byteData[] = md.digest();

                //convert the byte to hex format method 1
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < byteData.length; i++) {
                    sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
                }
                submission.password = sb.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            api.login(submission, cb);
        } else {
            cb.failure(null);
        }

    }
    public void Login(String token) {

    }
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null);
    }

}
