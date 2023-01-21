package com.notmiyouji.newsapp.java.NewsAPI;

import android.annotation.SuppressLint;
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
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.Category.ArticleCategory;
import com.notmiyouji.newsapp.kotlin.Utils;

import java.util.List;

public class NewsAdapterVertical extends RecyclerView.Adapter<NewsAdapterVertical.MyViewHolder> {


    AppCompatActivity activity;
    List<ArticleCategory> articleCategory;

    public NewsAdapterVertical(List<ArticleCategory> articleCategory, AppCompatActivity activity) {
        this.activity = activity;
        this.articleCategory = articleCategory;
    }

    @NonNull
    @Override
    public NewsAdapterVertical.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.news_items_vertical, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapterVertical.MyViewHolder holders, int position) {
        ArticleCategory model = articleCategory.get(position);
        String path = model.getUrlToImage();
        LoadImageURL loadImageURL = new LoadImageURL(path);
        loadImageURL.getImageFromURL(holders.imageView, holders);
        holders.title.setText(model.getTitle());
        holders.source.setText(model.getSource().getName());
        holders.time.setText(" \u2022 " + Utils.dateToTimeFormat(model.getPublishedAt()));
        holders.itemView.setOnClickListener(v -> {
            OpenNewsDetails openNewsDetails = new OpenNewsDetails(model.getUrl(),
                    model.getTitle(),
                    model.getUrlToImage(),
                    model.getUrl(),
                    model.getPublishedAt(), activity);
            openNewsDetails.openNewsDetails();
        });
    }

    @Override
    public int getItemCount() {
        return articleCategory.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<ArticleCategory> articleCategory) {
        this.articleCategory = articleCategory;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, source, time;
        ImageView imageView;
        private ItemClickListener itemClickListener;

        public MyViewHolder(View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.txtTitle);
            source = itemView.findViewById(R.id.txtSource);
            time = itemView.findViewById(R.id.txtPubDate);
            imageView = itemView.findViewById(R.id.imgNews);
        }
    }
}
