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

package com.notmiyouji.newsapp.java.rss2json;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.notmiyouji.newsapp.kotlin.RegEXImage;
import com.notmiyouji.newsapp.kotlin.activity.OpenNewsDetails;
import com.notmiyouji.newsapp.kotlin.model.Result;
import com.notmiyouji.newsapp.kotlin.sharedsettings.GetUserLogin;

import java.util.List;
import java.util.Objects;

public class Rss2JsonAdapterHorizontal extends RecyclerView.Adapter<Rss2JsonAdapterHorizontal.FeedViewHolder> {
    private final LayoutInflater inflater;
    private final List<Result> items;
    private final AppCompatActivity activity;
    private final GetUserLogin getUserLogin;
    private final NewsFavouriteByUser newsFavouriteByUser;
    String name;

    public Rss2JsonAdapterHorizontal(List<Result> items, AppCompatActivity activity) {
        this.items = items;
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        getUserLogin = new GetUserLogin(activity);
        newsFavouriteByUser = new NewsFavouriteByUser(activity);
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
        holder.txtSource.setText(name);
        String description = items.get(position).getDescription();
        String path;
        if (description != null) {
            RegEXImage regEXImage = new RegEXImage(description);
            path = regEXImage.regEXImage();
        } else {
            path = null;
        }
        LoadImageURL loadImageURL = new LoadImageURL(path);
        loadImageURL.getImageFromURL(holder.imageView, holder);
        if (Objects.requireNonNull(getUserLogin.getStatus()).equals("login")) {
            newsFavouriteByUser.checkFavouriteNewsInRecycleViewByUser(
                    getUserLogin.getUserID(),
                    items.get(position).getTitle(),
                    holder.favouriteBtn, holder.unfavouriteBtn);
        } else {
            holder.favouriteBtn.setVisibility(View.VISIBLE);
            holder.unfavouriteBtn.setVisibility(View.GONE);
        }
        holder.favouriteBtn.setOnClickListener(v -> {
            if (getUserLogin.getStatus().equals("login")) {
                newsFavouriteByUser.addFavouriteByUser(getUserLogin.getUserID(),
                        items.get(position).getLink(),
                        items.get(position).getTitle(),
                        path, items.get(position).getPubDate(), name);
                holder.favouriteBtn.setVisibility(View.GONE);
                holder.unfavouriteBtn.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(activity, R.string.please_login_to_use_this_feature, Toast.LENGTH_SHORT).show();
            }

        });
        holder.unfavouriteBtn.setOnClickListener(v -> {
            if (getUserLogin.getStatus().equals("login")) {
                newsFavouriteByUser.removeFavouriteByUser(getUserLogin.getUserID(), "", items.get(position).getTitle());
                holder.favouriteBtn.setVisibility(View.VISIBLE);
                holder.unfavouriteBtn.setVisibility(View.GONE);
            } else {
                Toast.makeText(activity, R.string.please_login_to_use_this_feature, Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnClickListener(v -> {
            String getPath;
            getPath = Objects.requireNonNullElse(path, "https://techvccloud.mediacdn.vn/2018/3/29/notavailableen-1522298007107364895792-21-0-470-800-crop-152229801290023105615.png");
            OpenNewsDetails openNewsDetails = new OpenNewsDetails(
                    items.get(position).getLink(),
                    items.get(position).getTitle(),
                    getPath,
                    items.get(position).getPubDate(), activity);
            openNewsDetails.openNewsDetails();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void clear() {
        int size = items.size();
        items.clear();
        notifyItemRangeRemoved(0, size);
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle, txtPubDate, txtSource;
        public ImageView imageView, favouriteBtn, unfavouriteBtn;
        public Activity activity;

        public FeedViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPubDate = itemView.findViewById(R.id.txtPubDate);
            txtSource = itemView.findViewById(R.id.txtSource);
            imageView = itemView.findViewById(R.id.imgNews);
            favouriteBtn = itemView.findViewById(R.id.favouriteBtn);
            unfavouriteBtn = itemView.findViewById(R.id.unfavouriteBtn);
            //Set Event
        }
    }
}
