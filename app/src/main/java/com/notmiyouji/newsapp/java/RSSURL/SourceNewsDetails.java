package com.notmiyouji.newsapp.java.RSSURL;

import android.app.ActivityOptions;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.RecycleViewAdapter.ListRSSAdapter;
import com.notmiyouji.newsapp.java.Retrofit.NewsAPPAPI;
import com.notmiyouji.newsapp.java.SharedSettings.LanguagePrefManager;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.FavouriteModel.SourceSubscribe;
import com.notmiyouji.newsapp.kotlin.FavouriteModel.SourceSubscribeCheck;
import com.notmiyouji.newsapp.kotlin.FavouriteModel.SourceUnsubscribe;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;
import com.notmiyouji.newsapp.kotlin.NetworkConnection;
import com.notmiyouji.newsapp.kotlin.RSSSource.ListObject;
import com.notmiyouji.newsapp.kotlin.RSSSource.RSSList;
import com.notmiyouji.newsapp.kotlin.RetrofitInterface.NewsAPPInterface;
import com.notmiyouji.newsapp.kotlin.SharedSettings.GetUserLogined;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadThemeShared;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import retrofit2.Call;

public class SourceNewsDetails extends AppCompatActivity {

    public String sourceID, imagePath, newSourceName, urlmainSource, newSourceDescription;
    public ImageView imageNews;
    public TextView sourceName, sourceDescription, newsTitle, urlmain;
    public RecyclerView rssRecycler;
    public Button subscribebtn, unsubscribebtn;
    GetUserLogined getUserLogined;
    LoadImageURL loadImageURL;
    NewsAPPInterface newsAPPInterface = NewsAPPAPI.getAPIClient().create(NewsAPPInterface.class);
    List<RSSList> rssLists = new ArrayList<>();
    LanguagePrefManager languagePrefManager;
    LoadThemeShared loadThemeShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        languagePrefManager = new LanguagePrefManager(this);
        languagePrefManager.setLocal(languagePrefManager.getLang());
        languagePrefManager.loadLocal();
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_news_details);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        //hooks
        getUserLogined = new GetUserLogined(this);
        //Check internet connection
        NetworkConnection networkConnection = new NetworkConnection(this);
        networkConnection.observe(this, networkConnection1 -> {
            if (!networkConnection1) {
                SourceNewsDetails.this.finish();
            }
        });
        imageNews = findViewById(R.id.imgNews);
        sourceName = findViewById(R.id.NewsSourceName);
        urlmain = findViewById(R.id.url_sourcemain);
        sourceDescription = findViewById(R.id.source_information);
        newsTitle = findViewById(R.id.SourceNewsDetailsTitle);
        rssRecycler = findViewById(R.id.rss_list);
        //Subscribe button and Unsubscribe button
        subscribebtn = findViewById(R.id.SubscribleBtn);
        unsubscribebtn = findViewById(R.id.UnSubscribleBtn);
        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //get value from intent
        sourceID = getIntent().getStringExtra("source_id");
        imagePath = getIntent().getStringExtra("source_image");
        newSourceName = getIntent().getStringExtra("source_name");
        urlmainSource = getIntent().getStringExtra("source_url");
        newSourceDescription = getIntent().getStringExtra("source_description");
        //load image news source
        loadImageURL = new LoadImageURL(imagePath);
        loadImageURL.loadImageforNewsDetails(imageNews);
        //set source name title, description
        sourceName.setText(newSourceName);
        urlmain.setText(urlmainSource);
        sourceDescription.setText(newSourceDescription);
        newsTitle.setText(newSourceName);
        //Load RSS List follow sourceName
        loadRSSRecycler(this, newSourceName);
        //Check subscribed
        switch (getUserLogined.getStatus()) {
            case "login":
                checkSubscribedEmail(sourceID, getUserLogined.getUserID());
                break;
            case "google":
                checkSubscribedSSO(sourceID, getUserLogined.getUserID());
                break;
            default:
                subscribebtn.setVisibility(View.VISIBLE);
                unsubscribebtn.setVisibility(View.GONE);
                break;
        }
        //Now subscribe and unsubscribe acction
        subscribebtn.setOnClickListener(v -> {
            switch (getUserLogined.getStatus()) {
                case "login":
                    subscribeEmail(sourceID, getUserLogined.getUserID());
                    break;
                case "google":
                    subscribeSSO(sourceID, getUserLogined.getUserID());
                    break;
                default:
                    Toast.makeText(this, "Please login to subscribe", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
        unsubscribebtn.setOnClickListener(v -> {
            switch (getUserLogined.getStatus()) {
                case "login":
                    unsubscribeEmail(sourceID, getUserLogined.getUserID());
                    break;
                case "google":
                    unsubscribeSSO(sourceID, getUserLogined.getUserID());
                    break;
                default:
                    break;
            }
        });
    }

    private void loadRSSRecycler(AppCompatActivity activity, String newSourceName) {
        Thread loadRSSList = new Thread(() -> {
            //load RSS List
            Call<ListObject> call = newsAPPInterface.getRSSList(newSourceName);
            assert call != null;
            call.enqueue(new retrofit2.Callback<ListObject>() {
                @Override
                public void onResponse(@NonNull Call<ListObject> call, @NonNull retrofit2.Response<ListObject> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (!rssLists.isEmpty()) {
                            rssLists.clear();
                        }
                        rssLists = response.body().getRssList();
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                        rssRecycler.setLayoutManager(linearLayoutManager);
                        ListRSSAdapter listRSSAdapter = new ListRSSAdapter(activity, rssLists);
                        rssRecycler.setAdapter(listRSSAdapter);
                        runOnUiThread(listRSSAdapter::notifyDataSetChanged);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ListObject> call, @NonNull Throwable t) {
                    Logger.getLogger("Error").warning(t.getMessage());
                }
            });
        });
        loadRSSList.start();
    }

    private void checkSubscribedEmail(String sourceID, String userID) {
        Call<SourceSubscribeCheck> checkSubscribed = newsAPPInterface.accountSourceSubscribeCheck(userID, sourceID);
        assert checkSubscribed != null;
        checkSubscribed.enqueue(new retrofit2.Callback<SourceSubscribeCheck>() {
            @Override
            public void onResponse(@NonNull Call<SourceSubscribeCheck> call, @NonNull retrofit2.Response<SourceSubscribeCheck> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        subscribebtn.setVisibility(View.GONE);
                        unsubscribebtn.setVisibility(View.VISIBLE);
                    }
                    else {
                        subscribebtn.setVisibility(View.VISIBLE);
                        unsubscribebtn.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<SourceSubscribeCheck> call, Throwable t) {

            }
        });
    }

    private void checkSubscribedSSO(String sourceID, String userID) {
        Call<SourceSubscribeCheck> checkSubscribed = newsAPPInterface.ssoSourceSubscribeCheck(userID, sourceID);
        assert checkSubscribed != null;
        checkSubscribed.enqueue(new retrofit2.Callback<SourceSubscribeCheck>() {
            @Override
            public void onResponse(@NonNull Call<SourceSubscribeCheck> call, @NonNull retrofit2.Response<SourceSubscribeCheck> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        subscribebtn.setVisibility(View.GONE);
                        unsubscribebtn.setVisibility(View.VISIBLE);
                    }
                    else {
                        subscribebtn.setVisibility(View.VISIBLE);
                        unsubscribebtn.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<SourceSubscribeCheck> call, Throwable t) {

            }
        });
    }

    private void subscribeEmail(String sourceID, String userID) {
        Call<SourceSubscribe> subscribe = newsAPPInterface.accountSourceSubscribe(userID, sourceID);
        assert subscribe != null;
        subscribe.enqueue(new retrofit2.Callback<SourceSubscribe>() {
            @Override
            public void onResponse(@NonNull Call<SourceSubscribe> call, @NonNull retrofit2.Response<SourceSubscribe> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        subscribebtn.setVisibility(View.GONE);
                        unsubscribebtn.setVisibility(View.VISIBLE);
                        Toast.makeText(SourceNewsDetails.this, "Subscribed", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(SourceNewsDetails.this, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SourceSubscribe> call, Throwable t) {

            }
        });
    }

    private void unsubscribeEmail(String sourceID, String userID) {
        Call<SourceUnsubscribe> unSubscribe = newsAPPInterface.accountSourceUnsubscribe(userID, sourceID);
        assert unSubscribe != null;
        unSubscribe.enqueue(new retrofit2.Callback<SourceUnsubscribe>() {
            @Override
            public void onResponse(@NonNull Call<SourceUnsubscribe> call, @NonNull retrofit2.Response<SourceUnsubscribe> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        subscribebtn.setVisibility(View.VISIBLE);
                        unsubscribebtn.setVisibility(View.GONE);
                        Toast.makeText(SourceNewsDetails.this, "Unsubscribed", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(SourceNewsDetails.this, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SourceUnsubscribe> call, Throwable t) {

            }
        });
    }

    private void subscribeSSO(String sourceID, String userID) {
        Call<SourceSubscribe> subscribe = newsAPPInterface.ssoSourceSubscribe(userID, sourceID);
        assert subscribe != null;
        subscribe.enqueue(new retrofit2.Callback<SourceSubscribe>() {
            @Override
            public void onResponse(@NonNull Call<SourceSubscribe> call, @NonNull retrofit2.Response<SourceSubscribe> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        subscribebtn.setVisibility(View.GONE);
                        unsubscribebtn.setVisibility(View.VISIBLE);
                        Toast.makeText(SourceNewsDetails.this, R.string.subscribed, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(SourceNewsDetails.this, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SourceSubscribe> call, Throwable t) {

            }
        });
    }

    private void unsubscribeSSO(String sourceID, String userID) {
        Call<SourceUnsubscribe> unSubscribe = newsAPPInterface.ssoSourceUnsubscribe(userID, sourceID);
        assert unSubscribe != null;
        unSubscribe.enqueue(new retrofit2.Callback<SourceUnsubscribe>() {
            @Override
            public void onResponse(@NonNull Call<SourceUnsubscribe> call, @NonNull retrofit2.Response<SourceUnsubscribe> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        subscribebtn.setVisibility(View.VISIBLE);
                        unsubscribebtn.setVisibility(View.GONE);
                        Toast.makeText(SourceNewsDetails.this, R.string.unsubscribed, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(SourceNewsDetails.this, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SourceUnsubscribe> call, Throwable t) {

            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        finish();
    }
}