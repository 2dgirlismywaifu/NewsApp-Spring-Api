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

package com.notmiyouji.newsapp.java.activity.newsdetail;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.activity.userlogin.NewsFavouriteByUser;
import com.notmiyouji.newsapp.kotlin.util.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.util.LoadUrlImage;
import com.notmiyouji.newsapp.kotlin.sharedsettings.AppContextWrapper;
import com.notmiyouji.newsapp.kotlin.sharedsettings.GetUserLogin;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadThemeShared;

import java.util.Objects;

public class NewsDetailByWebView extends AppCompatActivity {

    public WebView webView;
    public ImageView imgHeader;
    public String mUrl, mImg, mTitle, mSubTitle, mSource, mPubDate;
    private ProgressBar progressBar;
    private NewsFavouriteByUser newsFavouriteByUser;
    private GetUserLogin getUserLogin;

    protected void attachBaseContext(Context newBase) {
        //get language from shared preference
        LoadFollowLanguageSystem loadFollowLanguageSystem = new LoadFollowLanguageSystem(newBase);
        super.attachBaseContext(AppContextWrapper.wrap(newBase, Objects.requireNonNull(loadFollowLanguageSystem.getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoadThemeShared loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        newsFavouriteByUser = new NewsFavouriteByUser(this);
        getUserLogin = new GetUserLogin(this);
        //Progress Bar Setup
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setMax(100);
        progressBar.setProgress(1);
        ///////////////////////////////////////////////
        webView = findViewById(R.id.webView);
        imgHeader = findViewById(R.id.backdrop);
        TextView titlepreview = findViewById(R.id.txtTitle);
        TextView sourcepreview = findViewById(R.id.txtSource);
        TextView pubdatepreview = findViewById(R.id.txtPubDate);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        initCollapsingToolbar(); // Collapsing Toolbar
        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
        mImg = intent.getStringExtra("img");
        LoadUrlImage loadUrlImage = new LoadUrlImage(mImg);
        loadUrlImage.loadImageForNewsDetails(imgHeader);
        mTitle = intent.getStringExtra("title");
        mSubTitle = intent.getStringExtra("title");
        mSource = intent.getStringExtra("source");
        mPubDate = intent.getStringExtra("pubdate");

        titlepreview.setText(mTitle);
        sourcepreview.setText(mSource);
        pubdatepreview.setText(mPubDate);
        initWebView(mUrl, swipeRefreshLayout);
        //refresh web-view
        swipeRefreshLayout.setOnRefreshListener(() -> webView.reload());
    }

    public void onResume() {
        super.onResume();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView(String url, SwipeRefreshLayout swipeRefreshLayout) {
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    //UI Thread work here
                    webView.setWebChromeClient(new MyWebChromeClient(this, progressBar, swipeRefreshLayout));
                    webView.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon) {
                            super.onPageStarted(view, url, favicon);
                            invalidateOptionsMenu();
                        }

                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                            webView.loadUrl(url);
                            return true;
                        }

                        @Override
                        public void onPageFinished(WebView view, String url) {
                            super.onPageFinished(view, url);
                            swipeRefreshLayout.setRefreshing(false);
                            invalidateOptionsMenu();
                        }

                        @Override
                        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                            super.onReceivedError(view, request, error);
                            swipeRefreshLayout.setRefreshing(false);
                            progressBar.setVisibility(View.GONE);
                            invalidateOptionsMenu();
                        }

                        //SSL Error
                        @Override
                        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                            swipeRefreshLayout.setRefreshing(false);
                            progressBar.setVisibility(View.GONE);
                            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(view.getContext(), R.style.MaterialAlertDialog);
                            builder.setTitle(R.string.SSL_error_title);
                            String error_messeage = getString(R.string.firstline_ssl_error_messeage) + "\n" + error.toString() + "\n" + getString(R.string.check_ISP_network);
                            builder.setMessage(error_messeage);
                            builder.setNegativeButton("Cancel", (dialog, which) -> {
                                handler.cancel();
                                dialog.dismiss();
                            });
                            builder.create().show();
                        }
                    });
                    webView.clearCache(true);
                    webView.clearHistory();
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.getSettings().setLoadsImagesAutomatically(true);
                    webView.getSettings().setDomStorageEnabled(true);
                    webView.getSettings().setSupportZoom(true);
                    webView.getSettings().setBuiltInZoomControls(true);
                    webView.getSettings().setDisplayZoomControls(true);
                    webView.getSettings().setUseWideViewPort(true);
                    webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                    webView.setWebViewClient(new WebViewClient());
                    webView.loadUrl(url);

                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the txtPostTitle when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(mSource);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle("");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topbar_webview, menu);
        if (Objects.equals(getUserLogin.getStatus(), "login")) {
            newsFavouriteByUser.checkFavouriteNewsByUser(getUserLogin.getUserID(), mTitle,
                    menu.findItem(R.id.setfavourite), menu.findItem(R.id.setunfavourite));
        }

        menu.findItem(R.id.sharebtn).setOnMenuItemClickListener(item -> {
            //share news
            try {
                Intent sharenews = new Intent(Intent.ACTION_SEND);
                sharenews.setType("text/plan");
                sharenews.putExtra(Intent.EXTRA_SUBJECT, mSource);
                String body = mTitle + "\n" + mUrl + "\n" + getString(R.string.mainBody) + "\n";
                sharenews.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(sharenews, getString(R.string.ShareTitle)));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
            }
            return true;
        });
        menu.findItem(R.id.openbrowser).setOnMenuItemClickListener(item -> {
            //open in browser
            try {
                Intent openinbrowser = new Intent(Intent.ACTION_VIEW);
                openinbrowser.setData(Uri.parse(mUrl));
                startActivity(openinbrowser);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
            }
            return true;
        });
        menu.findItem(R.id.setfavourite).setOnMenuItemClickListener(item -> {
            //set favouriteController
            if (Objects.equals(getUserLogin.getStatus(), "login")) {//set favouriteController
                newsFavouriteByUser.addFavouriteByUser(getUserLogin.getUserID(),mUrl, mTitle, mImg, mPubDate);
                menu.findItem(R.id.setfavourite).setVisible(false);
                menu.findItem(R.id.setunfavourite).setVisible(true);
                return true;
            } else {
                Toast.makeText(this, R.string.please_login_to_use_this_feature, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        menu.findItem(R.id.setunfavourite).setOnMenuItemClickListener(item -> {
            //set un-favourite
            if (Objects.equals(getUserLogin.getStatus(), "login")) {//set favouriteController
                newsFavouriteByUser.removeFavouriteByUser(getUserLogin.getUserID(),"", mTitle);
                menu.findItem(R.id.setfavourite).setVisible(true);
                menu.findItem(R.id.setunfavourite).setVisible(false);
                return true;
            } else {
                Toast.makeText(this, R.string.please_login_to_use_this_feature, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        menu.findItem(R.id.help).setOnMenuItemClickListener(item -> {
            //help
            //Error URL, blank page
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog);
            builder.setTitle(R.string.help);
            builder.setMessage(R.string.error_loading_messeage);
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            builder.create().show();
            return true;
        });
        return super.onCreateOptionsMenu(menu);
    }
    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                ActivityOptions.makeSceneTransitionAnimation(NewsDetailByWebView.this).toBundle();
                finish();
            }
        }
    };

    public OnBackPressedCallback getCallback() {
        return callback;
    }

    private static class MyWebChromeClient extends WebChromeClient {
        Context context;
        ProgressBar progressBar;
        SwipeRefreshLayout swipeRefreshLayout;

        public MyWebChromeClient(Context context, ProgressBar progressBar, SwipeRefreshLayout swipeRefreshLayout) {
            super();
            this.context = context;
            this.progressBar = progressBar;
            this.swipeRefreshLayout = swipeRefreshLayout;
        }

        public void onProgressChanged(WebView view, int progress) {
            // Activities and WebViews measure progress with different scales.
            // The progress meter will automatically disappear when we reach 100%
            progressBar.setProgress(progress);
            if (progress == 100) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}
