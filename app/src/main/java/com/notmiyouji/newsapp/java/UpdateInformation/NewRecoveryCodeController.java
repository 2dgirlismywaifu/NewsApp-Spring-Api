package com.notmiyouji.newsapp.java.UpdateInformation;

import static com.notmiyouji.newsapp.java.Retrofit.NewsAPPAPI.getAPIClient;

import androidx.appcompat.app.AppCompatActivity;

import com.notmiyouji.newsapp.kotlin.RetrofitInterface.NewsAPPInterface;

public class NewRecoveryCodeController {
    String userid;
    AppCompatActivity activity;
    NewsAPPInterface newsAPPInterface = getAPIClient().create(NewsAPPInterface.class);

    public NewRecoveryCodeController(String userid, AppCompatActivity activity) {
        this.userid = userid;
        this.activity = activity;
    }


}
