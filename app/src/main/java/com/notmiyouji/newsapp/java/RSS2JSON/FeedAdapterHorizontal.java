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

package com.notmiyouji.newsapp.java.RSS2JSON;

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
import com.notmiyouji.newsapp.java.UpdateInformation.FavouriteController;
import com.notmiyouji.newsapp.kotlin.OpenActivity.OpenNewsDetails;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;
import com.notmiyouji.newsapp.kotlin.RSSFeed.Items;
import com.notmiyouji.newsapp.kotlin.RegEXImage;
import com.notmiyouji.newsapp.kotlin.SharedSettings.GetUserLogined;

import java.util.List;

public class FeedAdapterHorizontal extends RecyclerView.Adapter<FeedAdapterHorizontal.FeedViewHolder> {
    private final LayoutInflater inflater;

    List<Items> items;
    AppCompatActivity activity;
    GetUserLogined getUserLogined;
    FavouriteController favouriteController;
    String name;

    public FeedAdapterHorizontal(List<Items> items, AppCompatActivity activity, String name) {
        this.items = items;
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        getUserLogined = new GetUserLogined(activity);
        favouriteController = new FavouriteController(activity);
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
        switch (getUserLogined.getStatus()) {
            case "login":
                favouriteController.checkFavouriteEmailRecycleView(getUserLogined.getUserID(),
                        items.get(position).getLink(),
                        items.get(position).getTitle(),
                        path, name, holder.favouritebtn, holder.unfavouritebtn);
                break;
            case "google":
                favouriteController.checkFavouriteSSORecycleView(getUserLogined.getUserID(),
                        items.get(position).getLink(),
                        items.get(position).getTitle(),
                        path, name, holder.favouritebtn, holder.unfavouritebtn);
                break;
            default:
                holder.favouritebtn.setVisibility(View.VISIBLE);
                holder.unfavouritebtn.setVisibility(View.GONE);
                break;
        }
        holder.favouritebtn.setOnClickListener(v -> {
            switch (getUserLogined.getStatus()) {
                case "login":
                    favouriteController.addFavouriteEmail(getUserLogined.getUserID(),
                            items.get(position).getLink(),
                            items.get(position).getTitle(),
                            path,items.get(position).getPubDate(), name);
                    holder.favouritebtn.setVisibility(View.GONE);
                    holder.unfavouritebtn.setVisibility(View.VISIBLE);
                    break;
                case "google":
                    favouriteController.addFavouriteSSO(getUserLogined.getUserID(),
                            items.get(position).getLink(),
                            items.get(position).getTitle(),
                            path,items.get(position).getPubDate(), name);
                    holder.favouritebtn.setVisibility(View.GONE);
                    holder.unfavouritebtn.setVisibility(View.VISIBLE);
                    break;
                default:
                    Toast.makeText(activity, R.string.please_login_to_use_this_feature, Toast.LENGTH_SHORT).show();
                    break;
            }

        });
        holder.unfavouritebtn.setOnClickListener(v -> {
            switch (getUserLogined.getStatus()) {
                case "login":
                    favouriteController.removeFavouriteEmail(getUserLogined.getUserID(),
                            items.get(position).getLink(),
                            items.get(position).getTitle(),
                            path, name);
                    holder.favouritebtn.setVisibility(View.VISIBLE);
                    holder.unfavouritebtn.setVisibility(View.GONE);
                    break;
                case "google":
                    favouriteController.removeFavouriteSSO(getUserLogined.getUserID(),
                            items.get(position).getLink(),
                            items.get(position).getTitle(),
                            path, name);
                    holder.favouritebtn.setVisibility(View.VISIBLE);
                    holder.unfavouritebtn.setVisibility(View.GONE);
                    break;
                default:
                    Toast.makeText(activity, R.string.please_login_to_use_this_feature, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
        holder.itemView.setOnClickListener(v -> {
            String getpath;
            if (path != null) {
                getpath = path;
            }
            else {
                getpath = "https://techvccloud.mediacdn.vn/2018/3/29/notavailableen-1522298007107364895792-21-0-470-800-crop-152229801290023105615.png";
            }
            OpenNewsDetails openNewsDetails = new OpenNewsDetails(
                    items.get(position).getLink(),
                    items.get(position).getTitle(),
                    getpath,
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
        public ImageView imageView, favouritebtn, unfavouritebtn;
        public Activity activity;

        public FeedViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPubDate = itemView.findViewById(R.id.txtPubDate);
            txtsource = itemView.findViewById(R.id.txtSource);
            imageView = itemView.findViewById(R.id.imgNews);
            favouritebtn = itemView.findViewById(R.id.favouriteBtn);
            unfavouritebtn = itemView.findViewById(R.id.unfavouriteBtn);
            //Set Event
        }
    }
}
