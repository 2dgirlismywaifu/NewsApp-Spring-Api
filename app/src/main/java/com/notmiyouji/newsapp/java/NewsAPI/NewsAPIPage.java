package com.notmiyouji.newsapp.java.NewsAPI;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.RSSURL.FavouriteNews;
import com.notmiyouji.newsapp.java.RSSURL.HomePage;
import com.notmiyouji.newsapp.java.RSSURL.NewsAppAPI;
import com.notmiyouji.newsapp.java.RSSURL.SourceNewsList;
import com.notmiyouji.newsapp.java.global.LanguagePrefManager;
import com.notmiyouji.newsapp.java.global.NavigationPane;
import com.notmiyouji.newsapp.java.global.SettingsPage;
import com.notmiyouji.newsapp.java.global.recycleviewadapter.NewsAPITypeAdapter;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.CallSignInForm;
import com.notmiyouji.newsapp.kotlin.NewsAPIInterface;
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.Article;
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.Country;
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.News;
import com.notmiyouji.newsapp.kotlin.NewsAPPInterface;
import com.notmiyouji.newsapp.kotlin.sharedSettings.LoadWallpaperShared;
import com.notmiyouji.newsapp.kotlin.sharedSettings.SharedPreferenceSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsAPIPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    //Initialization variable
    public static final String API_KEY = new NewsAPIKey().getNEWSAPIKEY(); //the newsAPI key is here
    DrawerLayout drawerNewsAPI;
    NavigationView navigationView;
    LinearLayoutManager newstypeLayout;
    LinearLayoutManager newsAPIHorizontalLayout;
    RecyclerView newstypeView, newsViewHorizontal, newsViewVertical;
    Toolbar toolbar;
    NavigationPane navigationPane;
    NewsAPITypeAdapter newsAPITypeAdapter;
    Intent intent;
    List<Article> articles = new ArrayList<>(); //not include category
    NewsAdapterHorizontal newsAdapterHorizontal;
    NewsAPIInterface newsApiInterface = NewsAPIKey.getAPIClient().create(NewsAPIInterface.class);
    NewsAPPInterface newsAPPInterface = NewsAppAPI.getAPIClient().create(NewsAPPInterface.class);
    Call<News> call;
    LoadFollowCategory loadFollowCategory = new LoadFollowCategory();
    LoadWallpaperShared loadWallpaperShared;
    ExtendedFloatingActionButton filterCountry;
    TextView chooseTitle;
    TextInputLayout chooseHint;
    List<Country> countryList, codeList;
    SwipeRefreshLayout swipeRefreshLayout;
    LanguagePrefManager languagePrefManager;
    private String countryCodeDefault = "us";

    public String getCountryCodeDefault() {
        return countryCodeDefault;
    }

    public void setCountryCodeDefault(String countryCodeDefault) {
        this.countryCodeDefault = countryCodeDefault;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        languagePrefManager = new LanguagePrefManager(getBaseContext());
        languagePrefManager.setLocal(languagePrefManager.getLang());
        languagePrefManager.loadLocal();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_api_page);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        //Hooks
        drawerNewsAPI = findViewById(R.id.newsapi_page);
        navigationView = findViewById(R.id.nav_pane_view);
        toolbar = findViewById(R.id.nav_button);
        newstypeView = findViewById(R.id.news_type);
        newsViewHorizontal = findViewById(R.id.cardnews_view_horizontal);
        newsViewVertical = findViewById(R.id.cardnews_view_vertical);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        filterCountry = findViewById(R.id.filterCountry);
        //create navigation drawer
        navigationPane = new NavigationPane(drawerNewsAPI, this, toolbar, navigationView, R.id.newsapi_menu);
        navigationPane.CallFromUser();
        //From SharedPreference, change background for header navigation pane
        loadWallpaperShared = new LoadWallpaperShared(navigationView, this);
        loadWallpaperShared.loadWallpaper();
        //From SharedPreference, load country code
        reloadCountryCode();
        //open sign in page from navigationview
        CallSignInForm callSignInForm = new CallSignInForm(navigationView, this);
        callSignInForm.callSignInForm();
        //NewsCategory Type List
        LoadCategoryType(getCountryCodeDefault());
        LoadNewsAPI(getCountryCodeDefault());
        //open Country Filter
        openCountryFilter();
        //Hide float button when scroll recyclerview vertical
        hideWhenScroll();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            LoadNewsAPI(getCountryCodeDefault());
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void LoadCategoryType(String countryCodeDefault) {
        newsAPITypeAdapter = new NewsAPITypeAdapter(this, countryCodeDefault);
        newstypeLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        newstypeView.setLayoutManager(newstypeLayout);
        newstypeView.setAdapter(newsAPITypeAdapter);
        newsAPITypeAdapter.notifyDataSetChanged();
    }

    private void hideWhenScroll() {
        NestedScrollView nestedScrollView = findViewById(R.id.nested_scroll_view);
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (nestedScrollView.getScrollY() == 0) {
                filterCountry.extend();
            } else {
                filterCountry.shrink();
            }
        });
    }

    private void LoadNewsAPI(String countryCodeDefault) {
        //Loading Messeage
        final ProgressDialog mDialog = new ProgressDialog(this);
        mDialog.setMessage(this.getString(R.string.loading_messeage));
        mDialog.show();
        //Load JSONData and apply to RecycleView Horizontal Lastest NewsCategory
        LoadJSONLastestNews(this, mDialog, countryCodeDefault);
        //Load JSONData Business NewsCategory and apply to RecycleView Vertical Lastest NewsCategory
        loadFollowCategory.LoadJSONCategory(this, mDialog, "business", newsViewVertical, countryCodeDefault);
    }

    private void openCountryFilter() {
        filterCountry.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(NewsAPIPage.this);
            bottomSheetDialog.setContentView(R.layout.choose_feed);
            bottomSheetDialog.show();
            MaterialAutoCompleteTextView spinner_rss;
            spinner_rss = bottomSheetDialog.findViewById(R.id.spinner_rss);
            chooseTitle = bottomSheetDialog.findViewById(R.id.choose_title);
            chooseHint = bottomSheetDialog.findViewById(R.id.hint_to_choose);
            chooseTitle.setText(R.string.choose_country_newsapi);
            chooseHint.setHint(R.string.choose_country_hint);
            call = newsAPPInterface.getListCountry();
            countryList = new ArrayList<>();
            assert call != null;
            call.enqueue(new Callback<News>() {
                @Override
                public void onResponse(@NonNull Call<News> call, @NonNull Response<News> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getCountrylist() != null) {
                            if(!countryList.isEmpty()){
                                countryList.clear();
                            }
                            countryList = response.body().getCountrylist();
                            List<Country> countryListName = countryList;
                            ArrayList<String> countryName = new ArrayList<>();
                            for (Country country : countryListName) {
                                countryName.add(country.getCountryName());
                            }
                            assert spinner_rss != null;
                            spinner_rss.setAdapter(new ArrayAdapter<>(NewsAPIPage.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, countryName));
                        }
                    }
                }
                @Override
                public void onFailure(@NonNull Call<News> call, @NonNull Throwable t) {
                    Logger.getLogger("Error").warning(t.getMessage());
                }
            });
            Button okbtn = bottomSheetDialog.findViewById(R.id.btnLoad);
            assert okbtn != null;
            assert spinner_rss != null;
            okbtn.setOnClickListener(v1 -> {
                String countryName = spinner_rss.getText().toString();
                if (countryName.isEmpty()) {
                    Toast.makeText(NewsAPIPage.this, R.string.country_not_choose, Toast.LENGTH_SHORT).show();
                } else {
                    loadCountryCode(countryName);
                    bottomSheetDialog.dismiss();
                }

            });
        });
    }

    private void loadCountryCode(String countryName) {
        call = newsAPPInterface.getCountryCode(countryName);
        codeList = new ArrayList<>();
        assert call != null;
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(@NonNull Call<News> call, @NonNull Response<News> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getCountrycode() != null) {
                        if(!codeList.isEmpty()){
                            codeList.clear();
                        }
                        codeList = response.body().getCountrycode();
                        Country country = codeList.get(0);
                        String code = country.getCountryCode();
                        //Save shared preference
                        SharedPreferenceSettings sharedPreferenceSettings = new SharedPreferenceSettings(NewsAPIPage.this);
                        assert code != null;
                        sharedPreferenceSettings.getSharedCountry(code);
                        setCountryCodeDefault(code);
                        LoadCategoryType(code);
                        //load newsapi again
                        LoadNewsAPI(code);
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<News> call, @NonNull Throwable t) {
                Logger.getLogger("Error").warning(t.getMessage());
            }
        });
    }

    public void LoadJSONLastestNews(AppCompatActivity activity, ProgressDialog mDialog, String country){
        Thread loadSourceAPI = new Thread(() -> {
            call = newsApiInterface.getLatestNews(country, API_KEY);
            assert call != null;
            call.enqueue(new Callback<News>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(@NonNull Call<News> call, @NonNull Response<News> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getArticle() != null) {
                            if (!articles.isEmpty()) {
                                articles.clear();
                            }
                            articles = response.body().getArticle();
                            newsAPIHorizontalLayout = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                            newsViewHorizontal.setLayoutManager(newsAPIHorizontalLayout);
                            newsAdapterHorizontal = new NewsAdapterHorizontal(articles, activity);
                            newsViewHorizontal.setAdapter(newsAdapterHorizontal);
                            newsAdapterHorizontal.notifyDataSetChanged();
                        }
                    }
                }
                @Override
                public void onFailure(@NonNull Call<News> call, @NonNull Throwable t) {
                    Toast.makeText(NewsAPIPage.this, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
                }
            });

            runOnUiThread(() ->
                    newsViewHorizontal.getViewTreeObserver().addOnGlobalLayoutListener(mDialog::dismiss));
        });
        loadSourceAPI.start();
    }
    private void reloadCountryCode(){
        SharedPreferences sharedPreferences = getSharedPreferences("CountryCode", MODE_PRIVATE);
        if(!sharedPreferences.getString("code", "").equals(getCountryCodeDefault())){
            setCountryCodeDefault(sharedPreferences.getString("code", getCountryCodeDefault()));
        }
    }
    @Override
    public void onBackPressed() {
        if (drawerNewsAPI.isDrawerOpen(GravityCompat.START)) {
            drawerNewsAPI.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int menuitem = item.getItemId();
        if (menuitem == R.id.home_menu) {
            intent = new Intent(NewsAPIPage.this, HomePage.class);
            startActivity(intent);
        }
        else if (menuitem == R.id.source_menu) {
            intent = new Intent(NewsAPIPage.this, SourceNewsList.class);
            startActivity(intent);
        }
        else if (menuitem == R.id.favourite_menu) {
            intent = new Intent(NewsAPIPage.this, FavouriteNews.class);
            startActivity(intent);
        }
        else if (menuitem == R.id.settings_menu) {
            intent = new Intent(NewsAPIPage.this, SettingsPage.class);
            startActivity(intent);
        }
        return true;
    }

    public void onResume() {
        super.onResume();
        loadWallpaperShared.loadWallpaper();
        languagePrefManager.loadLocal();
    }
}