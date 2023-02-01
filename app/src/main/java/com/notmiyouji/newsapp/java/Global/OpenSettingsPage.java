package com.notmiyouji.newsapp.java.Global;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.notmiyouji.newsapp.java.Global.Signed.SettingsLogined;
import com.notmiyouji.newsapp.kotlin.SharedSettings.GetUserLogined;

public class OpenSettingsPage {
    AppCompatActivity activity;
    SharedPreferences sharedPreferences;
    public OpenSettingsPage(AppCompatActivity activity) {
        this.activity = activity;
    }
    public void openSettings() {
        GetUserLogined getUserLogined = new GetUserLogined(activity);
        if (getUserLogined.getStatus().equals("login")) {
            activity.startActivity(new Intent(activity, SettingsLogined.class));
        } else {

            activity.startActivity(new Intent(activity, SettingsPage.class));
        }
    }
}
