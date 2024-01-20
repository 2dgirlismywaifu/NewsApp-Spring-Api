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

package com.notmiyouji.newsapp.java.activity;

import android.app.ActivityOptions;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.recycleview.ListRssAdapter;
import com.notmiyouji.newsapp.kotlin.retrofit.NewsAppApi;
import com.notmiyouji.newsapp.kotlin.util.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.util.LoadUrlImage;
import com.notmiyouji.newsapp.kotlin.util.NetworkConnection;
import com.notmiyouji.newsapp.kotlin.retrofit.NewsAppInterface;
import com.notmiyouji.newsapp.kotlin.util.AppUtils;
import com.notmiyouji.newsapp.kotlin.model.NewsAppResult;
import com.notmiyouji.newsapp.kotlin.model.rss2json.RSSList;
import com.notmiyouji.newsapp.kotlin.model.rss2json.SourceSubscribe;
import com.notmiyouji.newsapp.kotlin.sharedsettings.GetUserLogin;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LanguagePrefManager;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadThemeShared;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import retrofit2.Call;

public class SourceNewsDetails extends AppCompatActivity {

    public String sourceID, imagePath, newSourceName, urlMainSource, newSourceDescription;
    public ImageView imageNews;
    public TextView sourceName, sourceDescription, newsTitle, urlMain;
    public RecyclerView rssRecycler;
    public Button subscribeBtn, unsubscribeBtn;
    private GetUserLogin getUserLogin;
    private final NewsAppInterface newsAPPInterface = NewsAppApi.getApiClient().create(NewsAppInterface.class);
    private List<RSSList> rssLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LanguagePrefManager languagePrefManager = new LanguagePrefManager(this);
        languagePrefManager.setLocal(languagePrefManager.getLang());
        languagePrefManager.loadLocal();
        LoadThemeShared loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_news_details);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        //hooks
        getUserLogin = new GetUserLogin(this);
        //Check internet connection
        NetworkConnection networkConnection = new NetworkConnection(this);
        networkConnection.observe(this, networkConnection1 -> {
            if (!networkConnection1) {
                finish();
            }
        });
        imageNews = findViewById(R.id.imgNews);
        sourceName = findViewById(R.id.NewsSourceName);
        urlMain = findViewById(R.id.url_sourcemain);
        sourceDescription = findViewById(R.id.source_information);
        newsTitle = findViewById(R.id.SourceNewsDetailsTitle);
        rssRecycler = findViewById(R.id.rss_list);
        //Subscribe button and Unsubscribe button
        subscribeBtn = findViewById(R.id.SubscribleBtn);
        unsubscribeBtn = findViewById(R.id.UnSubscribleBtn);
        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //get value from intent
        sourceID = getIntent().getStringExtra("sourceId");
        imagePath = getIntent().getStringExtra("sourceImage");
        newSourceName = getIntent().getStringExtra("sourceName");
        urlMainSource = getIntent().getStringExtra("sourceUrl");
        newSourceDescription = getIntent().getStringExtra("sourceDescription");
        //load image news source
        LoadUrlImage loadUrlImage = new LoadUrlImage(imagePath);
        loadUrlImage.loadImageForNewsDetails(imageNews);
        //set source name title, description
        sourceName.setText(newSourceName);
        urlMain.setText(urlMainSource);
        sourceDescription.setText(newSourceDescription);
        newsTitle.setText(newSourceName);
        //Load RSS List follow sourceName
        loadRSSRecycler(this, newSourceName);
        //Check subscribed
        if (Objects.equals(getUserLogin.getStatus(), "login")) {
            checkSubscribedSource(sourceID, getUserLogin.getUserID());
        } else {
            subscribeBtn.setVisibility(View.VISIBLE);
            unsubscribeBtn.setVisibility(View.GONE);
        }
        //Now subscribe and unsubscribe action
        subscribeBtn.setOnClickListener(v -> {
            if (Objects.equals(getUserLogin.getStatus(), "login")) {
                subscribeNewsSource(sourceID, getUserLogin.getUserID());
            } else {
                Toast.makeText(this, R.string.please_login_to_use_this_feature, Toast.LENGTH_SHORT).show();
            }
        });
        unsubscribeBtn.setOnClickListener(v -> {
            if (Objects.equals(getUserLogin.getStatus(), "login")) {
                unsubscribeNewsSource(sourceID, getUserLogin.getUserID());
            } else {
                Toast.makeText(this, R.string.please_login_to_use_this_feature, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadRSSRecycler(AppCompatActivity activity, String newSourceName) {
        Thread loadRSSList = new Thread(() -> {
            //load RSS List
            Call<NewsAppResult> call = newsAPPInterface.getRssListFollowSource(AppUtils.encodeToBase64(newSourceName));
            assert call != null;
            call.enqueue(new retrofit2.Callback<>() {
                @Override
                public void onResponse(@NonNull Call<NewsAppResult> call, @NonNull retrofit2.Response<NewsAppResult> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (!rssLists.isEmpty()) {
                            rssLists.clear();
                        }
                        rssLists = response.body().getListRss();
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                        rssRecycler.setLayoutManager(linearLayoutManager);
                        ListRssAdapter listRSSAdapter = new ListRssAdapter(activity, rssLists);
                        rssRecycler.setAdapter(listRSSAdapter);
                        runOnUiThread(listRSSAdapter::notifyDataSetChanged);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<NewsAppResult> call, @NonNull Throwable t) {
                    Logger.getLogger("Error").warning(t.getMessage());
                }
            });
        });
        loadRSSList.start();
    }

    private void checkSubscribedSource(String sourceID, String userID) {
        Call<SourceSubscribe> checkSubscribed = newsAPPInterface.accountCheckSourceSubscribe(
                AppUtils.encodeToBase64(userID),
                AppUtils.encodeToBase64(sourceID));
        assert checkSubscribed != null;
        checkSubscribed.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<SourceSubscribe> call, @NonNull retrofit2.Response<SourceSubscribe> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (Objects.equals(response.body().getStatus(), "found")) {
                        subscribeBtn.setVisibility(View.GONE);
                        unsubscribeBtn.setVisibility(View.VISIBLE);
                    } else {
                        subscribeBtn.setVisibility(View.VISIBLE);
                        unsubscribeBtn.setVisibility(View.GONE);
                        Toast.makeText(SourceNewsDetails.this, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SourceSubscribe> call, @NonNull Throwable t) {
                Toast.makeText(SourceNewsDetails.this, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void subscribeNewsSource(String sourceId, String userId) {
        Call<SourceSubscribe> subscribe = newsAPPInterface.accountSubscribeNewsSource(userId, sourceId);
        assert subscribe != null;
        subscribe.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<SourceSubscribe> call, @NonNull retrofit2.Response<SourceSubscribe> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (Objects.equals(response.body().getStatus(), "success")) {
                        subscribeBtn.setVisibility(View.GONE);
                        unsubscribeBtn.setVisibility(View.VISIBLE);
                        Toast.makeText(SourceNewsDetails.this, R.string.subscribed, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SourceNewsDetails.this, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SourceSubscribe> call, @NonNull Throwable t) {

            }
        });
    }

    private void unsubscribeNewsSource(String sourceId, String userId) {
        Call<SourceSubscribe> unSubscribe = newsAPPInterface.unSubscribeNewsSource( userId, sourceId);
        assert unSubscribe != null;
        unSubscribe.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<SourceSubscribe> call, @NonNull retrofit2.Response<SourceSubscribe> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (Objects.equals(response.body().getStatus(), "deleted")) {
                        subscribeBtn.setVisibility(View.VISIBLE);
                        unsubscribeBtn.setVisibility(View.GONE);
                        Toast.makeText(SourceNewsDetails.this, R.string.unsubscribed, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SourceNewsDetails.this, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SourceSubscribe> call, @NonNull Throwable t) {
                Toast.makeText(SourceNewsDetails.this, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Replace deprecated onBackPressed() method
    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            ActivityOptions.makeSceneTransitionAnimation(SourceNewsDetails.this).toBundle();
            finish();
        }
    };

    public OnBackPressedCallback getCallback() {
        return callback;
    }
}