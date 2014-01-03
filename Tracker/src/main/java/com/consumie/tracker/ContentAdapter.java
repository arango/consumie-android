package com.consumie.tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.consumie.tracker.models.Content;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Arango on 1/2/14.
 */
public class ContentAdapter extends ArrayAdapter<Content> {
        private final Context context;
        private final List<Content> content;


        public ContentAdapter(Context context, int resource, List<Content> objects) {
            super(context, resource, objects);
            this.context = context;
            this.content = objects;
        }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_item_content, parent, false);
        Content c = content.get(position);
        TextView contentTitle = (TextView)rowView.findViewById(R.id.content_title);
        TextView contentSubtitle = (TextView)rowView.findViewById(R.id.content_subtitle);
        ImageView contentImage = (ImageView)rowView.findViewById(R.id.content_image);
        Picasso.with(this.context).load(c.contentimage_path).into(contentImage);
        contentTitle.setText(c.name);
        contentSubtitle.setText(c.subtitle);

        return rowView;
    }
}
