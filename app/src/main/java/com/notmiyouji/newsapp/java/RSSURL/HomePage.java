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

package com.notmiyouji.newsapp.java.RSSURL;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.Category.RssURLCategory;
import com.notmiyouji.newsapp.kotlin.SharedSettings.AppContextWrapper;
import com.notmiyouji.newsapp.kotlin.CheckNetworkConnection;
import com.notmiyouji.newsapp.java.Global.FavouriteNews;
import com.notmiyouji.newsapp.java.Global.MaterialAltertLoading;
import com.notmiyouji.newsapp.java.Global.NavigationPane;
import com.notmiyouji.newsapp.java.NewsAPI.NewsAPIPage;
import com.notmiyouji.newsapp.java.RSS2JSON.FeedMultiRSS;
import com.notmiyouji.newsapp.java.RecycleViewAdapter.NewsTypeAdapter;
import com.notmiyouji.newsapp.java.Retrofit.NewsAPPAPI;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.NetworkConnection;
import com.notmiyouji.newsapp.kotlin.OpenActivity.CallSignInForm;
import com.notmiyouji.newsapp.kotlin.OpenActivity.OpenSettingsPage;
import com.notmiyouji.newsapp.kotlin.RSSSource.ListObject;
import com.notmiyouji.newsapp.kotlin.RSSSource.NewsSource;
import com.notmiyouji.newsapp.kotlin.RetrofitInterface.NewsAPPInterface;
import com.notmiyouji.newsapp.kotlin.SharedSettings.GetUserLogined;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadNavigationHeader;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadThemeShared;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadWallpaperShared;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadWallpaperSharedLogined;
import com.notmiyouji.newsapp.kotlin.SharedSettings.SharedPreferenceSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import retrofit2.Call;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Initialization variable
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayoutManager linearLayoutManager, newsViewLayoutHorizontal;
    LinearLayout homepageScreen, errorInternet;
    RecyclerView recyclerView, newsViewHorizontal, newsViewVertical;
    Toolbar toolbar;
    NavigationPane navigationPane;
    Intent intent;
    LoadWallpaperShared loadWallpaperShared;
    LoadWallpaperSharedLogined loadWallpaperSharedLogined;
    ExtendedFloatingActionButton filterSource;
    TextView chooseTitle;
    EditText searchNews;
    TextInputLayout chooseHint;
    SwipeRefreshLayout swipeRefreshLayout;
    FeedMultiRSS feedMultiRSS;
    TextView welcomeText;
    LoadNavigationHeader loadNavigationHeader;
    NewsAPPInterface newsAPPInterface = NewsAPPAPI.getAPIClient().create(NewsAPPInterface.class);
    List<NewsSource> newsSources = new ArrayList<>();
    LoadFollowLanguageSystem loadFollowLanguageSystem;
    LoadThemeShared loadThemeShared;
    GetUserLogined getUserLogined;
    CheckBox syncSubscribe;
    MaterialAutoCompleteTextView spinner_rss;
    CheckNetworkConnection checkNetworkConnection;

    private String deafultSource = "VNExpress";
    public String getDeafultSource() {
        return deafultSource;
    }
    public void setDeafultSource(String deafultSource) {
        this.deafultSource = deafultSource;
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        //get language from shared preference
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(newBase);
        super.attachBaseContext(AppContextWrapper.wrap(newBase,loadFollowLanguageSystem.getLanguage()));
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        //Hooks
        welcomeText = findViewById(R.id.welcome_title);
        homepageScreen = findViewById(R.id.homepage_screen);
        errorInternet = findViewById(R.id.noInternetScreen);
        filterSource = findViewById(R.id.filterSource);
        NetworkConnection networkConnection = new NetworkConnection(this);
        networkConnection.observe(this, isConnected -> {
            if (isConnected) {
                homepageScreen.setVisibility(android.view.View.VISIBLE);
                filterSource.setVisibility(android.view.View.VISIBLE);
                errorInternet.setVisibility(android.view.View.GONE);
            } else {
                homepageScreen.setVisibility(android.view.View.GONE);
                filterSource.setVisibility(android.view.View.GONE);
                errorInternet.setVisibility(android.view.View.VISIBLE);
            }
        });
        navigationView = findViewById(R.id.nav_pane_view);
        //From sharedPreference, if user logined saved, call navigation pane with user name header
        loadNavigationHeader = new LoadNavigationHeader(this, navigationView);
        loadNavigationHeader.loadHeader();
        //From SharedPreference, change background for header navigation pane
        getUserLogined = new GetUserLogined(this);
        if (getUserLogined.getStatus().equals("login") || getUserLogined.getStatus().equals("google")) {
            String welcomUserName = getString(R.string.user_welcome) + " @" + getUserLogined.getUsername() + "\n" + getString(R.string.user_welcome_2);
            welcomeText.setText(welcomUserName);
            loadWallpaperSharedLogined = new LoadWallpaperSharedLogined(navigationView, this);
            loadWallpaperSharedLogined.loadWallpaper();
        } else {
            welcomeText.setText(R.string.Guest_welcome);
            loadWallpaperShared = new LoadWallpaperShared(navigationView, this);
            loadWallpaperShared.loadWallpaper();
        }
        drawerLayout = findViewById(R.id.home_page);
        toolbar = findViewById(R.id.nav_button);
        recyclerView = findViewById(R.id.news_type);
        newsViewHorizontal = findViewById(R.id.cardnews_view_horizontal);
        newsViewVertical = findViewById(R.id.cardnews_view_vertical);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        navigationPane = new NavigationPane(drawerLayout, this, toolbar, navigationView, R.id.home_menu);
        navigationPane.CallFromUser();
        //From SharedPreference, call back source name
        reloadData();
        //NewsCategory Type List
        LoadCategory(getDeafultSource());
        //open sign in page from navigationview
        if ("".equals(getUserLogined.getStatus())) {
            CallSignInForm callSignInForm = new CallSignInForm(navigationView, this);
            callSignInForm.callSignInForm();
        }
        checkNetworkConnection = new CheckNetworkConnection();
        if (checkNetworkConnection.CheckConnection(this)) {
            //Load news from source
            LoadSourceNews(getDeafultSource());
        }
        //Select source to load (Settings will save to shared preference)
        openSourceChoose();
        //User progress bar
        //Hide float button when scroll recyclerview vertical
        hideWhenScroll();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            LoadSourceNews(getDeafultSource());
            swipeRefreshLayout.setRefreshing(false);
        });
        //Search with RecyclerView Filter
        searchNews = findViewById(R.id.search_input);
        searchNews.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                feedMultiRSS.filterHorizonal(s.toString());
                //Still can't filter vertical recyclerview, only horizontal can filter
                //feedMultiRSS.filterVertical(s.toString());
            }
        });
    }


    //Collapse float button
    private void hideWhenScroll() {
        NestedScrollView homepageScroll = findViewById(R.id.homepageScroll);
        homepageScroll.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (homepageScroll.getScrollY() > 0) {
                filterSource.shrink();
            } else {
                filterSource.extend();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void LoadCategory(String source) {
        NewsTypeAdapter newsTypeAdapter = new NewsTypeAdapter(this, source);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(newsTypeAdapter);
        newsTypeAdapter.notifyDataSetChanged();
    }

    private void LoadSourceNews(String source) {
        MaterialAltertLoading materialAltertLoading = new MaterialAltertLoading(this);
        MaterialAlertDialogBuilder mDialog = materialAltertLoading.getDiaglog();
        AlertDialog alertDialog = mDialog.create();
        alertDialog.show();
        //Load Lastest News
        Thread loadLastNews = new Thread(() -> {
            newsViewLayoutHorizontal = new LinearLayoutManager(HomePage.this, LinearLayoutManager.HORIZONTAL, false);
            feedMultiRSS = new FeedMultiRSS(HomePage.this, newsViewHorizontal, newsViewLayoutHorizontal);
            feedMultiRSS.MultiFeedHorizontal("BreakingNews", source, alertDialog);
        });
        loadLastNews.start();

        //load NewsView Vertical
        RssURLCategory rssURLCategory = new RssURLCategory(HomePage.this, newsViewVertical, alertDialog, source);
        rssURLCategory.startLoad("BreakingNews");
    }


    private void openSourceChoose() {
        filterSource.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(HomePage.this);
            bottomSheetDialog.setContentView(R.layout.choose_feed);
            bottomSheetDialog.show();
            spinner_rss = bottomSheetDialog.findViewById(R.id.spinner_rss);
            chooseTitle = bottomSheetDialog.findViewById(R.id.choose_title);
            chooseHint = bottomSheetDialog.findViewById(R.id.hint_to_choose);
            syncSubscribe = bottomSheetDialog.findViewById(R.id.checkSubscrible);
            chooseTitle.setText(R.string.choose_rss);
            chooseHint.setHint(R.string.Select_RSS_Feed);
            assert spinner_rss != null;
            assert syncSubscribe != null;
            //if checkbox is checked, load source from user account
            syncSubscribe.setOnClickListener(v1 -> {
                if (syncSubscribe.isChecked()) {
                    String statusAccount = getUserLogined.getStatus();
                    if (statusAccount != null) {
                        switch (statusAccount) {
                            case "login":
                                //clear spinner_rss
                                spinner_rss.setText("");
                                AccountSourceList(spinner_rss, getUserLogined.getUserID());
                                break;
                            case "google":
                                spinner_rss.setText("");
                                SSOSourceList(spinner_rss, getUserLogined.getUserID());
                                break;
                            default:
                                syncSubscribe.setChecked(false);
                                Toast.makeText(HomePage.this, R.string.please_login_to_use_this_feature, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                } else {
                    spinner_rss.setText("");
                    GuestSourceList(spinner_rss);
                }
            });
            spinner_rss.setText("");
            GuestSourceList(spinner_rss);
            Button okbtn = bottomSheetDialog.findViewById(R.id.btnLoad);
            assert okbtn != null;
            okbtn.setOnClickListener(v1 -> {
                String sourceName = spinner_rss.getText().toString();
                if (sourceName.isEmpty()) {
                    Toast.makeText(HomePage.this, R.string.source_not_choose, Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferenceSettings sharedPreferenceSettings = new SharedPreferenceSettings(HomePage.this);
                    sharedPreferenceSettings.getSharedSource(sourceName);
                    setDeafultSource(sourceName);
                    bottomSheetDialog.dismiss();
                    LoadCategory(sourceName);
                    LoadSourceNews(sourceName);
                }

            });
        });
    }

    private void GuestSourceList(MaterialAutoCompleteTextView spinner_rss) {
        Call<ListObject> call = newsAPPInterface.getAllSource();
        assert call != null;
        call.enqueue(new retrofit2.Callback<ListObject>() {
            @Override
            public void onResponse(@NonNull Call<ListObject> call, @NonNull retrofit2.Response<ListObject> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getNewsSource() != null) {
                        if (!newsSources.isEmpty()) {
                            newsSources.clear();
                        }
                        newsSources = response.body().getNewsSource();
                        List<NewsSource> newsSourceList = newsSources;
                        ArrayList<String> listSource = new ArrayList<>();
                        for (NewsSource newsSource : newsSourceList) {
                            listSource.add(newsSource.getSource_name());
                        }
                        assert spinner_rss != null;
                        spinner_rss.setAdapter(new ArrayAdapter<>(HomePage.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listSource));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ListObject> call, @NonNull Throwable t) {
                Logger.getLogger("Error").warning(t.getMessage());
            }
        });
    }

    private void AccountSourceList(MaterialAutoCompleteTextView spinner_rss, String userid) {
        Call<ListObject> call = newsAPPInterface.accountAllSource(userid);
        assert call != null;
        call.enqueue(new retrofit2.Callback<ListObject>() {
            @Override
            public void onResponse(@NonNull Call<ListObject> call, @NonNull retrofit2.Response<ListObject> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getNewsSource() != null) {
                        if (!newsSources.isEmpty()) {
                            newsSources.clear();
                        }
                        newsSources = response.body().getNewsSource();
                        List<NewsSource> newsSourceList = newsSources;
                        ArrayList<String> listSource = new ArrayList<>();
                        for (NewsSource newsSource : newsSourceList) {
                            listSource.add(newsSource.getSource_name());
                        }
                        assert spinner_rss != null;
                        spinner_rss.setAdapter(new ArrayAdapter<>(HomePage.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listSource));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ListObject> call, @NonNull Throwable t) {
                Logger.getLogger("Error").warning(t.getMessage());
            }
        });
    }

    private void SSOSourceList(MaterialAutoCompleteTextView spinner_rss, String userid) {
        Call<ListObject> call = newsAPPInterface.ssoAllSource(userid);
        assert call != null;
        call.enqueue(new retrofit2.Callback<ListObject>() {
            @Override
            public void onResponse(@NonNull Call<ListObject> call, @NonNull retrofit2.Response<ListObject> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getNewsSource() != null) {
                        if (!newsSources.isEmpty()) {
                            newsSources.clear();
                        }
                        newsSources = response.body().getNewsSource();
                        List<NewsSource> newsSourceList = newsSources;
                        ArrayList<String> listSource = new ArrayList<>();
                        for (NewsSource newsSource : newsSourceList) {
                            listSource.add(newsSource.getSource_name());
                        }
                        assert spinner_rss != null;
                        spinner_rss.setAdapter(new ArrayAdapter<>(HomePage.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listSource));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ListObject> call, @NonNull Throwable t) {
                Logger.getLogger("Error").warning(t.getMessage());
            }
        });
    }

    private void reloadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("SourceName", MODE_PRIVATE);
        if (!sharedPreferences.getString("name", "").equals(getDeafultSource())) {
            setDeafultSource(sharedPreferences.getString("name", getDeafultSource()));
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int menuitem = item.getItemId();
        switch (menuitem) {
            case R.id.newsapi_menu:
                intent = new Intent(HomePage.this, NewsAPIPage.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.source_menu:
                intent = new Intent(HomePage.this, SourceNewsList.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.favourite_menu:
                intent = new Intent(HomePage.this, FavouriteNews.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.settings_menu:
                OpenSettingsPage openSettingsPage = new OpenSettingsPage(HomePage.this);
                openSettingsPage.openSettings();
                break;
        }
        return true;
    }

    public void onResume() {
        super.onResume();
        if (getUserLogined.getStatus().equals("login") || getUserLogined.getStatus().equals("google")) {
            loadWallpaperSharedLogined = new LoadWallpaperSharedLogined(navigationView, this);
            loadWallpaperSharedLogined.loadWallpaper();
        } else {
            loadWallpaperShared = new LoadWallpaperShared(navigationView, this);
            loadWallpaperShared.loadWallpaper();
        }
        navigationPane = new NavigationPane(drawerLayout, this, toolbar, navigationView, R.id.home_menu);
        navigationPane.CallFromUser();
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
    }
}