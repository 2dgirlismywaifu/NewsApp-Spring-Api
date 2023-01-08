package com.notmiyouji.newsapp.java.NewsAPI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.global.NewsDetail;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.Article;
import com.notmiyouji.newsapp.kotlin.Utils;

import java.util.List;

public class NewsAdapterHorizontal extends RecyclerView.Adapter<NewsAdapterHorizontal.MyViewHolder> {


    AppCompatActivity activity;
    List<Article> articles;

    public NewsAdapterHorizontal(List<Article> articles, AppCompatActivity activity) {
        this.activity = activity;
        this.articles = articles;
    }

    @NonNull
    @Override
    public NewsAdapterHorizontal.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.news_items_horizontal, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NewsAdapterHorizontal.MyViewHolder holders, int position) {
        Article model = articles.get(position);
        String path = model.getUrlToImage();
        LoadImageURL loadImageURL = new LoadImageURL(path);
        loadImageURL.getImageFromURL(holders.imageView, holders);
        holders.title.setText(model.getTitle());
        holders.source.setText(model.getSource().getName());
        holders.time.setText(" \u2022 " + Utils.DateToTimeFormat(model.getPublishedAt()));
        holders.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(activity, NewsDetail.class);
            intent.putExtra("url", model.getUrl());
            intent.putExtra("title", model.getTitle());
            intent.putExtra("img", model.getUrlToImage());
            intent.putExtra("source", model.getUrl());
            intent.putExtra("pubdate", model.getPublishedAt());
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, source, time;
        ShapeableImageView imageView;
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
