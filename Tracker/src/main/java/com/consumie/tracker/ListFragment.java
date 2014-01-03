package com.consumie.tracker;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ListView;

import com.consumie.tracker.models.ConsumptionOptions;
import com.consumie.tracker.models.Content;
import com.consumie.tracker.models.ContentList;
import com.consumie.tracker.models.Results;
import com.consumie.tracker.util.API;
import com.consumie.tracker.util.APICallback;
import com.consumie.tracker.util.BaseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Arango on 1/2/14.
 */
public class ListFragment extends Fragment {
    private BaseActivity activity;
    private ContentList data;
    private List<Content> contents;
    private ListView contentList;
    private ContentAdapter adapter;
    private ConsumptionOptions opts;
    private Integer windowHeight;
    private Boolean gettingContent = false;
    public ListFragment(BaseActivity activity) {
            this.activity = activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        contentList = (ListView)rootView.findViewById(R.id.list);
        opts = new ConsumptionOptions();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        int width = display.getWidth();
        windowHeight = display.getHeight();
        TimeZone tz = TimeZone.getDefault();
        opts.uimg = "80,80";
        opts.img = "300,300";
        opts.timezone = tz.getRawOffset() / 1000 / -60;
        opts.source = "friends";
        contents = new ArrayList<Content>();
        adapter = new ContentAdapter(this.activity, R.id.list, contents);
        contentList.setAdapter(adapter);
        contentList.setOnScrollListener(scrollListener);

        getContent();
        return rootView;
    }
    public AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener(){

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView lw, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {

                    final int lastItem = firstVisibleItem + visibleItemCount;
                    if(lastItem >= totalItemCount - 4) {
                        getContent();
                    }
        }
    };
    public void getContent() {
        if (gettingContent)
            return;
        gettingContent = true;
        activity.api.Consumption(opts, new APICallback(activity) {
            @Override
            public void success(Results results, Response response) {
                super.success(results, response);
                data = results.ContentList;
                opts.start_point = data.nextID;
                if (data.results != null) {
                    for (Content item : data.results)
                        adapter.add(item);
                }
                adapter.notifyDataSetChanged();
                gettingContent = false;
            }
            @Override
            public void failure(RetrofitError err) {
                super.failure(err);
                gettingContent = false;
            }
        });
    }
}
