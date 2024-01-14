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

package com.notmiyouji.newsapp.java.newsdetails;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsIntent;

//Introducing the Chrome Custom Tabs. Faster and more secure than webview.
public class NewsDetailsChrome {
    private final String url;
    private final String title;
    private final String img;
    private final String source;
    private final String pubdate;
    private final Activity activity;
    Intent intent;

    public NewsDetailsChrome(String url, String title, String img, String source, String pubdate, Activity activity) {
        this.url = url;
        this.title = title;
        this.img = img;
        this.source = source;
        this.pubdate = pubdate;
        this.activity = activity;
    }

    public void openNewsDetails() {
        //Make sure chrome is installed
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setStartAnimations(activity, android.R.anim.fade_in, android.R.anim.fade_out);
        builder.setExitAnimations(activity, android.R.anim.fade_in, android.R.anim.fade_out);
        intent = new Intent(activity, NewsDetailsChrome.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        intent.putExtra("img", img);
        intent.putExtra("source", source);
        intent.putExtra("pubdate", pubdate);
        builder.setUrlBarHidingEnabled(false);
        builder.setShowTitle(true);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(activity, Uri.parse(url));
    }
}