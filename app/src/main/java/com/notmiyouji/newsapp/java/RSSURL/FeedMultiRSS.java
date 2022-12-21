package com.notmiyouji.newsapp.java.RSSURL;

import android.app.ProgressDialog;
import android.util.Base64;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.notmiyouji.newsapp.java.global.AzureSQLServer;
import com.notmiyouji.newsapp.kotlin.RSSFeed.RSSObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FeedMultiRSS {

    static {
        System.loadLibrary("keys");
    }

    public native String getRSS2JSONAPIKey();

    public final String RSS2JSON_API_KEY = new String(android.util.Base64.decode(getRSS2JSONAPIKey(), Base64.DEFAULT));
    AzureSQLServer azureSQLServer = new AzureSQLServer();
    PreparedStatement ps;
    AppCompatActivity activity;
    LinearLayoutManager linearLayoutManager;

    public FeedMultiRSS(AppCompatActivity activity, RecyclerView recyclerView, LinearLayoutManager linearLayoutManager) {
        this.activity = activity;
        this.recyclerView = recyclerView;
        this.linearLayoutManager = linearLayoutManager;
    }

    RecyclerView recyclerView;
    RSSObject rssObject;

    public final String READ = "SELECT * FROM NEWS_DETAIL WHERE url_type = ?";

    public String getREAD() {
        return READ;
    }
    public void MultiFeedHorizontal(String url_type, ProgressDialog progressDialog) {
        try {
            Connection con = azureSQLServer.getConnection();
            ps = con.prepareStatement(getREAD());
            ps.setString(1, url_type);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               Thread startFeed = new Thread(new Runnable() {
                   @Override
                   public void run() {
                       try {
                           String RSS_to_Json_API = "https://api.rss2json.com/v1/api.json?rss_url=" + rs.getString("url") + "&api_key=" + RSS2JSON_API_KEY;
                           HTTPDataHandler http = new HTTPDataHandler();
                           String result = http.GetHTTPData(RSS_to_Json_API);
                           activity.runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   rssObject = new Gson().fromJson(result, RSSObject.class);
                                   FeedAdapterHorizontal adapter = new FeedAdapterHorizontal(rssObject, activity);
                                   recyclerView.setLayoutManager(linearLayoutManager);
                                   recyclerView.setAdapter(adapter);
                                   recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(progressDialog::dismiss);
                               }
                           });
                       } catch (SQLException e) {
                           e.printStackTrace();
                       }

                   }
               });
               startFeed.start();
            }
            rs.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void MultiFeedVertical(String url_type, ProgressDialog progressDialog) {
        try {
            Connection con = azureSQLServer.getConnection();
            ps = con.prepareStatement(getREAD());
            ps.setString(1, url_type);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Thread startFeed = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String RSS_to_Json_API = "https://api.rss2json.com/v1/api.json?rss_url=" + rs.getString("url") + "&api_key=" + RSS2JSON_API_KEY;
                            HTTPDataHandler http = new HTTPDataHandler();
                            String result = http.GetHTTPData(RSS_to_Json_API);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    rssObject = new Gson().fromJson(result, RSSObject.class);
                                    FeedAdapterVertical adapter = new FeedAdapterVertical(rssObject, activity);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(progressDialog::dismiss);
                                }
                            });
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                });
                startFeed.start();
            }
            rs.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
