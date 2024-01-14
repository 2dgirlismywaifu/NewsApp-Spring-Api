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

package com.notmiyouji.newsapp.java.category;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.java.rss2json.Rss2JsonMultiFeed;

public class RssUrlCategory {

    private final RecyclerView newsViewVertical;
    private final AppCompatActivity activity;
    private final AlertDialog alertDialog;
    private final String userId;
    private LinearLayoutManager newsViewLayoutVertical;

    public RssUrlCategory(AppCompatActivity activity, RecyclerView newsViewVertical, AlertDialog alertDialog, String userId) {
        this.activity = activity;
        this.newsViewVertical = newsViewVertical;
        this.alertDialog = alertDialog;
        this.userId = userId;
    }

    public void startLoad(String type) {
        Thread loadNewsVerticalMode = new Thread(() -> {
            newsViewLayoutVertical = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            Rss2JsonMultiFeed feedMultiRSS = new Rss2JsonMultiFeed(activity, newsViewVertical, newsViewLayoutVertical);
            feedMultiRSS.rss2JsonVertical(userId, type, alertDialog);
        });
        loadNewsVerticalMode.start();
    }
}
