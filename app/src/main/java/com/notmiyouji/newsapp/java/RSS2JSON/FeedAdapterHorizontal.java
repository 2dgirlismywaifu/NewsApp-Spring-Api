package com.notmiyouji.newsapp.java.RSS2JSON;

import android.annotation.SuppressLint;
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
import com.notmiyouji.newsapp.java.NewsDetails.OpenNewsDetails;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;
import com.notmiyouji.newsapp.kotlin.RSSFeed.Items;
import com.notmiyouji.newsapp.kotlin.RegEXImage;

import java.util.List;

public class FeedAdapterHorizontal extends RecyclerView.Adapter<FeedAdapterHorizontal.FeedViewHolder> {
    private final LayoutInflater inflater;

    List<Items> items;
    AppCompatActivity activity;
    String name;

    public FeedAdapterHorizontal(List<Items> items, AppCompatActivity activity, String name) {
        this.items = items;
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        this.name = name;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.news_items_horizontal, parent, false);
        return new FeedViewHolder(itemView);
    }


    @SuppressLint("RtlHardcoded")
    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {

        holder.txtTitle.setText(items.get(position).getTitle());
        holder.txtPubDate.setText(items.get(position).getPubDate());
        holder.txtsource.setText(name);
        RegEXImage regEXImage = new RegEXImage(items.get(position).getDescription(), items.get(position).getThumbnail());
        String path = regEXImage.regEXImage();
        LoadImageURL loadImageURL = new LoadImageURL(path);
        loadImageURL.getImageFromURL(holder.imageView, holder);
        holder.itemView.setOnClickListener(v -> {
            OpenNewsDetails openNewsDetails = new OpenNewsDetails(
                    items.get(position).getLink(),
                    items.get(position).getTitle(),
                    path,
                    name,
                    items.get(position).getPubDate(), activity);
            openNewsDetails.openNewsDetails();
        });
    }

    @Override
    public int getItemCount() {
        try {
            return items.size();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void clear() {
        int size = items.size();
        items.clear();
        notifyItemRangeRemoved(0, size);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<Items> filterList2) {
        this.items = filterList2;
        notifyDataSetChanged();
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle, txtPubDate, txtsource;
        public ImageView imageView;
        public Activity activity;

        public FeedViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPubDate = itemView.findViewById(R.id.txtPubDate);
            txtsource = itemView.findViewById(R.id.txtSource);
            imageView = itemView.findViewById(R.id.imgNews);
            //Set Event
        }
    }
}
