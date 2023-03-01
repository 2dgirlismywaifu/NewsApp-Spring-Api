/*
 * Copyright By @2dgirlismywaifu (2023) .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.notmiyouji.newsapp.java.NewsAPI;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.UpdateInformation.FavouriteController;
import com.notmiyouji.newsapp.kotlin.OpenActivity.OpenNewsDetails;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.Article;
import com.notmiyouji.newsapp.kotlin.SharedSettings.GetUserLogined;
import com.notmiyouji.newsapp.kotlin.Utils;

import java.util.List;
import java.util.Objects;

public class NewsAdapterHorizontal extends RecyclerView.Adapter<NewsAdapterHorizontal.MyViewHolder> {

    GetUserLogined getUserLogined;
    FavouriteController favouriteController;
    AppCompatActivity activity;
    List<Article> articles;

    public NewsAdapterHorizontal(List<Article> articles, AppCompatActivity activity) {
        this.activity = activity;
        getUserLogined = new GetUserLogined(activity);
        favouriteController = new FavouriteController(activity);
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
        holders.time.setText(" \u2022 " + Utils.dateToTimeFormat(model.getPublishedAt()));
        switch (Objects.requireNonNull(getUserLogined.getStatus())) {
            case "login":
                favouriteController.checkFavouriteEmailRecycleView(getUserLogined.getUserID(),
                        model.getUrl(),
                        model.getTitle(),
                        path, model.getSource().getName(), holders.favouritebtn, holders.unfavouritebtn);
                break;
            case "google":
                favouriteController.checkFavouriteSSORecycleView(getUserLogined.getUserID(),
                        model.getUrl(),
                        model.getTitle(),
                        path, model.getSource().getName(), holders.favouritebtn, holders.unfavouritebtn);
                break;
            default:
                holders.favouritebtn.setVisibility(View.VISIBLE);
                holders.unfavouritebtn.setVisibility(View.GONE);
                break;
        }
        holders.favouritebtn.setOnClickListener(v -> {
            switch (getUserLogined.getStatus()) {
                case "login":
                    favouriteController.addFavouriteEmail(getUserLogined.getUserID(),
                            model.getUrl(),
                            model.getTitle(),
                            path, model.getPublishedAt(), model.getSource().getName());
                    holders.favouritebtn.setVisibility(View.GONE);
                    holders.unfavouritebtn.setVisibility(View.VISIBLE);
                    break;
                case "google":
                    favouriteController.addFavouriteSSO(getUserLogined.getUserID(),
                            model.getUrl(),
                            model.getTitle(),
                            path,model.getPublishedAt(), model.getSource().getName());
                    holders.favouritebtn.setVisibility(View.GONE);
                    holders.unfavouritebtn.setVisibility(View.VISIBLE);
                    break;
                default:
                    Toast.makeText(activity, R.string.please_login_to_use_this_feature, Toast.LENGTH_SHORT).show();
                    break;
            }

        });
        holders.unfavouritebtn.setOnClickListener(v -> {
            switch (getUserLogined.getStatus()) {
                case "login":
                    favouriteController.removeFavouriteEmail(getUserLogined.getUserID(),
                            model.getUrl(),
                            model.getTitle(),
                            path, model.getSource().getName());
                    holders.favouritebtn.setVisibility(View.VISIBLE);
                    holders.unfavouritebtn.setVisibility(View.GONE);
                    break;
                case "google":
                    favouriteController.removeFavouriteSSO(getUserLogined.getUserID(),
                            model.getUrl(),
                            model.getTitle(),
                            path, model.getSource().getName());
                    holders.favouritebtn.setVisibility(View.VISIBLE);
                    holders.unfavouritebtn.setVisibility(View.GONE);
                    break;
                default:
                    Toast.makeText(activity, R.string.please_login_to_use_this_feature, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
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
        return articles.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<Article> articles) {
        this.articles = articles;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, source, time;
        ImageView favouritebtn, unfavouritebtn;
        ShapeableImageView imageView;

        public MyViewHolder(View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.txtTitle);
            source = itemView.findViewById(R.id.txtSource);
            time = itemView.findViewById(R.id.txtPubDate);
            imageView = itemView.findViewById(R.id.imgNews);
            favouritebtn = itemView.findViewById(R.id.favouriteBtn);
            unfavouritebtn = itemView.findViewById(R.id.unfavouriteBtn);
        }
    }
}
