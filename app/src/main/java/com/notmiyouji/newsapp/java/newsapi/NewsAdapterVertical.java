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

package com.notmiyouji.newsapp.java.newsapi;

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

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.activity.userlogin.NewsFavouriteByUser;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;
import com.notmiyouji.newsapp.kotlin.Utils;
import com.notmiyouji.newsapp.kotlin.activity.OpenNewsDetails;
import com.notmiyouji.newsapp.kotlin.newsapi.Article;
import com.notmiyouji.newsapp.kotlin.sharedsettings.GetUserLogin;

import java.util.List;
import java.util.Objects;

public class NewsAdapterVertical extends RecyclerView.Adapter<NewsAdapterVertical.MyViewHolder> {

    private final GetUserLogin getUserLogin;
    private final NewsFavouriteByUser newsFavouriteByUser;
    private final AppCompatActivity activity;
    private List<Article> article;

    public NewsAdapterVertical(List<Article> article, AppCompatActivity activity) {
        this.activity = activity;
        getUserLogin = new GetUserLogin(activity);
        newsFavouriteByUser = new NewsFavouriteByUser(activity);
        this.article = article;
    }

    @NonNull
    @Override
    public NewsAdapterVertical.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.news_items_vertical, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NewsAdapterVertical.MyViewHolder holders, int position) {
        Article model = article.get(position);
        String path = model.getUrlToImage();
        System.out.println(path);
        LoadImageURL loadImageURL = new LoadImageURL(path);
        loadImageURL.getImageFromURL(holders.imageView, holders);
        holders.title.setText(model.getTitle());
        holders.source.setText(Objects.requireNonNull(model.getSource()).getName());
        holders.time.setText(" • " + Utils.dateToTimeFormat(model.getPublishedAt()));
        if (Objects.requireNonNull(getUserLogin.getStatus()).equals("login")) {
            newsFavouriteByUser.checkFavouriteNewsInRecycleViewByUser(
                    getUserLogin.getUserID(), model.getTitle(),
                    holders.favouriteBtn, holders.unFavouriteBtn);
        } else {
            holders.favouriteBtn.setVisibility(View.VISIBLE);
            holders.unFavouriteBtn.setVisibility(View.GONE);
        }
        holders.favouriteBtn.setOnClickListener(v -> {
            if (getUserLogin.getStatus().equals("login")) {
                newsFavouriteByUser.addFavouriteByUser(getUserLogin.getUserID(),model.getUrl(), model.getTitle(),
                        path, model.getPublishedAt(), model.getSource().getName());
                holders.favouriteBtn.setVisibility(View.GONE);
                holders.unFavouriteBtn.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(activity, R.string.please_login_to_use_this_feature, Toast.LENGTH_SHORT).show();
            }

        });
        holders.unFavouriteBtn.setOnClickListener(v -> {
            if (getUserLogin.getStatus().equals("login")) {
                newsFavouriteByUser.removeFavouriteByUser(getUserLogin.getUserID(),"", model.getTitle());
                holders.favouriteBtn.setVisibility(View.VISIBLE);
                holders.unFavouriteBtn.setVisibility(View.GONE);
            } else {
                Toast.makeText(activity, R.string.please_login_to_use_this_feature, Toast.LENGTH_SHORT).show();
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
        return article.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<Article> article) {
        this.article = article;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, source, time;
        ImageView imageView, favouriteBtn, unFavouriteBtn;

        public MyViewHolder(View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.txtTitle);
            source = itemView.findViewById(R.id.txtSource);
            time = itemView.findViewById(R.id.txtPubDate);
            imageView = itemView.findViewById(R.id.imgNews);
            favouriteBtn = itemView.findViewById(R.id.favouriteBtn);
            unFavouriteBtn = itemView.findViewById(R.id.unfavouriteBtn);
        }
    }
}
