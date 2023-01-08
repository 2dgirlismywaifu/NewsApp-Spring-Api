package com.notmiyouji.newsapp.java.RSSURL;

import android.app.ActivityOptions;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.global.LanguagePrefManager;
import com.notmiyouji.newsapp.java.global.recycleviewadapter.ListRSSAdapter;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;
import com.notmiyouji.newsapp.kotlin.NewsAPPInterface;
import com.notmiyouji.newsapp.kotlin.RSSSource.ListObject;
import com.notmiyouji.newsapp.kotlin.RSSSource.RSSList;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import retrofit2.Call;

public class SourceNewsDetails extends AppCompatActivity {

    public String imagePath, newSourceName, urlmainSource,newSourceDescription;
    public ImageView imageNews;
    public TextView sourceName, sourceDescription, newsTitle, urlmain;
    public RecyclerView rssRecycler;
    LoadImageURL loadImageURL;
    NewsAPPInterface newsAPPInterface = NewsAppAPI.getAPIClient().create(NewsAPPInterface.class);
    List<RSSList> rssLists = new ArrayList<>();
    LanguagePrefManager languagePrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        languagePrefManager = new LanguagePrefManager(getBaseContext());
        languagePrefManager.setLocal(languagePrefManager.getLang());
        languagePrefManager.loadLocal();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_news_details);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        //hooks
        imageNews = findViewById(R.id.imgNews);
        sourceName = findViewById(R.id.NewsSourceName);
        urlmain = findViewById(R.id.url_sourcemain);
        sourceDescription = findViewById(R.id.source_information);
        newsTitle = findViewById(R.id.SourceNewsDetailsTitle);
        rssRecycler = findViewById(R.id.rss_list);
        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //get value from intent
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
        loadRSSRecycler(this,newSourceName);
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
    public void onBackPressed() {
        super.onBackPressed();
        ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        finish();
    }
}