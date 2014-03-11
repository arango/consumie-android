package com.consumie.tracker;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.consumie.tracker.models.AddCommentOptions;
import com.consumie.tracker.models.Comment;
import com.consumie.tracker.models.Consumption;
import com.consumie.tracker.models.Content;
import com.consumie.tracker.models.ContentList;
import com.consumie.tracker.models.Results;
import com.consumie.tracker.util.APICallback;
import com.consumie.tracker.util.BaseActivity;
import com.consumie.tracker.util.ObservableScrollView;
import com.consumie.tracker.util.ScrollViewListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Arango on 1/2/14.
 */
public class ContentFragment extends Fragment {
    Content value;
    int position;
    TextView sub;
    Display display;
    BaseActivity ctx;
    ObservableScrollView sv;
    LinearLayout content_holder;
    View overlay;
    ImageView background;
    ActionBar mActionBar;
    Boolean sendingComment = false;
    View layoutView;
    LayoutInflater inflater;

    public ContentFragment() {
    }

    public ContentFragment(Content c, BaseActivity ctx) {
        value = c;
        this.ctx = ctx;
        this.mActionBar = ((ActionBarActivity)ctx).getSupportActionBar();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments() != null ? getArguments().getInt("val") : 1;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        layoutView = inflater.inflate(R.layout.fragment_list, container, false);
        overlay = layoutView.findViewById(R.id.ovrlay);
        sv = (ObservableScrollView)layoutView.findViewById(R.id.scrollView);
        sv.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                Float alpha = y / 200f;
                int padding = 0 - (y / 3);
                if (alpha <= .75)
                    overlay.setAlpha(alpha);
                if (padding <= 0 && padding >= -50) {
                    background.setPadding(0, padding, 0, 0);
                }
            }
        });
        TextView tv = (TextView)layoutView.findViewById(R.id.title);
        tv.setText(value.name);
        sub = (TextView)layoutView.findViewById(R.id.subtitle);
        sub.setText(value.subtitle);
        content_holder = (LinearLayout)layoutView.findViewById(R.id.content_holder);

        display = ctx.getWindowManager().getDefaultDisplay();
        ViewTreeObserver obs = sub.getViewTreeObserver();
        obs.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int top = sub.getTop();
                int height = sub.getHeight();

                content_holder.setPadding(0, overlay.getHeight() - top - height - 5, 0, 0);
                sv.setVisibility(View.VISIBLE);
                sub.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        background = (ImageView)layoutView.findViewById(R.id.background);
        int id = getResources().getIdentifier("com.consumie.tracker:drawable/placeholder_" + value.type, null, null);
        //Picasso.with(getActivity()).load(value.contentimage_path).placeholder(id).into(background);
        Picasso.with(getActivity()).load(value.contentimage_path).into(background);
        createConsumption();
        return layoutView;
    }
    public void createConsumption() {
        LinearLayout consumption = (LinearLayout)layoutView.findViewById(R.id.consumption);
        consumption.removeAllViews();
        for (int i = 0; i < value.users.length; i++) {
            Consumption c = value.users[i];
            View subview = inflater.inflate(R.layout.list_item_consumption, null);
            ImageView userImage = (ImageView)subview.findViewById(R.id.user_image);
            Picasso.with(getActivity()).load(c.userimage_path).into(userImage);
            TextView userRating = (TextView)subview.findViewById(R.id.user_rating);
            if (c.rating == null || c.rating < 0)
                userRating.setVisibility(View.GONE);
            else
                userRating.setText(String.valueOf(c.rating));
            TextView userInfo = (TextView)subview.findViewById(R.id.user_info);
            userInfo.setText("Consumed by\n" + c.username);
            mActionBar.setTitle(c.created);
            LinearLayout comments = (LinearLayout)subview.findViewById(R.id.comments);
            for (int j = 0; j < c.comment_list.length; j++) {
                Comment cmt = c.comment_list[j];
                View subsubview = inflater.inflate(R.layout.list_item_comment, null);
                TextView commenterName = (TextView)subsubview.findViewById(R.id.commenter_name);
                TextView comment = (TextView)subsubview.findViewById(R.id.commenter_comment);
                commenterName.setText(cmt.user);
                comment.setText(cmt.text);
                comments.addView(subsubview);
                View divider = new View(ctx);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 1);
                lp.setMargins(0, 8, 0, 8);
                divider.setLayoutParams(lp);
                divider.setBackgroundColor(getResources().getColor(R.color.medium_light));
                comments.addView(divider);
            }
            EditText addComment = (EditText)subview.findViewById(R.id.add_comment);
            addComment.setTag(c.id);
            addComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE ||
                            actionId == KeyEvent.KEYCODE_ENTER ||
                            actionId == EditorInfo.IME_ACTION_NEXT ||
                            (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN)
                            ) {
                        sendComment((EditText) v);
                    }
                    return false;
                }
            });
            consumption.addView(subview);
        }
    }
    public void sendComment(EditText addComment) {
        if (sendingComment)
            return;
        AddCommentOptions opt = new AddCommentOptions();
        opt.notes = addComment.getText().toString().trim();
        opt.consumptionID = Integer.parseInt(addComment.getTag().toString());
        this.ctx.api.AddComment(opt, new APICallback(this.ctx.api) {
            @Override
            public void success(Results results, Response response) {
                super.success(results, response);
                for (int i = 0; i < value.users.length; i++) {
                    Consumption c = value.users[i];
                    if (c.id == results.Comment.consumptionID) {
                        results.Comment.user = ctx.api.getUser().username;
                        Comment[] newComments = new Comment[c.comment_list.length + 1];
                        for (int j = 0; j < c.comment_list.length; j++) {
                            newComments[j] = c.comment_list[j];
                        }
                        newComments[newComments.length - 1] = results.Comment;
                        c.comment_list = newComments;
                        value.users[i] = c;
                        break;
                    }
                }
                createConsumption();
                sendingComment = false;
            }
            @Override
            public void failure(RetrofitError err) {
                super.failure(err);
                sendingComment = false;
            }

        });
        sendingComment = true;
    }

}