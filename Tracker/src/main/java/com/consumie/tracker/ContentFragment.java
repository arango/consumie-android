package com.consumie.tracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.consumie.tracker.models.Content;
import com.consumie.tracker.util.BaseActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Arango on 1/2/14.
 */
public class ContentFragment extends Fragment {
    Content value;
    int position;

    public ContentFragment() {
    }

    public ContentFragment(Content c) {
        value = c;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments() != null ? getArguments().getInt("val") : 1;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.fragment_list, container, false);
        TextView tv = (TextView)layoutView.findViewById(R.id.title);
        tv.setText(value.name);
        tv.setVisibility(View.VISIBLE);
        TextView sub = (TextView)layoutView.findViewById(R.id.subtitle);
        sub.setText(value.subtitle);
        sub.setVisibility(View.VISIBLE);
        ImageView background = (ImageView)layoutView.findViewById(R.id.background);
        int id = getResources().getIdentifier("com.consumie.tracker:drawable/placeholder_" + value.type, null, null);
        background.setImageResource(id);
        Picasso.with(getActivity()).load(value.contentimage_path).into(background);
        return layoutView;
    }


}