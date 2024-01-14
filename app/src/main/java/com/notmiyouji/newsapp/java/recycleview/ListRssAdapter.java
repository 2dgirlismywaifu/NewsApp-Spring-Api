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

package com.notmiyouji.newsapp.java.recycleview;

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
import com.notmiyouji.newsapp.kotlin.model.RSSList;

import java.util.List;

public class ListRssAdapter extends RecyclerView.Adapter<ListRssAdapter.ListSourceHolder> {

    AppCompatActivity activity;
    List<RSSList> rssSourceList;

    public ListRssAdapter(AppCompatActivity activity, List<RSSList> rssSourceList) {
        this.activity = activity;
        this.rssSourceList = rssSourceList;
    }
    @NonNull
    @Override
    public ListSourceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.source_news_details_layout, parent, false);
        return new ListSourceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListSourceHolder holder, int position) {
        RSSList rssList = rssSourceList.get(position);
        String title = activity.getString(R.string.list_rss_type_title) + rssList.getUrlType();
        holder.rssTitle.setText(title);
        String url = activity.getString(R.string.rss_url) + rssList.getUrl();
        holder.rss_url.setText(url);
        String path = rssList.getUrlImage();
        LoadImageURL loadImageURL = new LoadImageURL(path);
        loadImageURL.getImageFromURL(holder.source_image, holder);
        //Picasso.get().load(path).into(holder.source_image);
    }

    @Override
    public int getItemCount() {
        return rssSourceList.size();
    }

    public static class ListSourceHolder extends RecyclerView.ViewHolder {
        TextView rssTitle, rss_url;
        ImageView source_image;

        public ListSourceHolder(@NonNull View itemView) {
            super(itemView);
            rssTitle = itemView.findViewById(R.id.rss_type);
            rss_url = itemView.findViewById(R.id.rss_url);
            source_image = itemView.findViewById(R.id.imgNews);
        }
    }
}