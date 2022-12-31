package com.notmiyouji.newsapp.java.RSSURL;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;
import com.notmiyouji.newsapp.kotlin.RSSFeed.RSSObject;

public class FeedAdapterVertical extends RecyclerView.Adapter<FeedAdapterVertical.FeedViewVerticalHolder> {

    RSSObject rssObject;
    AppCompatActivity activity;
    private final LayoutInflater inflater;

    public FeedAdapterVertical(RSSObject rssObject, AppCompatActivity activity) {
        this.rssObject = rssObject;
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }

    @NonNull
    @Override
    public FeedViewVerticalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.news_items_vertical,parent,false);
        return new FeedViewVerticalHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeedViewVerticalHolder holder, int position) {

        holder.txtTitle.setText(rssObject.getItems().get(position).getTitle());
        holder.txtPubDate.setText(rssObject.getItems().get(position).getPubDate());
        holder.txtsource.setText(rssObject.getFeed().getTitle());
//        try
//        {
//            String path = rssObject.getItems().get(position).getThumbnail();
//            Picasso.get().load(path).into(holder.imageView);
//        }
//        catch (IllegalArgumentException e)
//        {
//            e.printStackTrace();
//
//        }
        String path = rssObject.getItems().get(position).getThumbnail();
        LoadImageURL loadImageURL = new LoadImageURL(path);
        loadImageURL.getImageFromURL(holder.imageView, holder);
    }

    @Override
    public int getItemCount() {
        try
        {
            return rssObject.getItems().size();
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
            return 0;
        }

    }

    public static class FeedViewVerticalHolder extends RecyclerView.ViewHolder
            {

        public TextView txtTitle,txtPubDate,txtsource;
        public ImageView imageView;
        public Activity activity;


        public FeedViewVerticalHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPubDate = itemView.findViewById(R.id.txtPubDate);
            txtsource = itemView.findViewById(R.id.txtSource);
            imageView = itemView.findViewById(R.id.imgNews);
            //Set Event
        }
    }
}
