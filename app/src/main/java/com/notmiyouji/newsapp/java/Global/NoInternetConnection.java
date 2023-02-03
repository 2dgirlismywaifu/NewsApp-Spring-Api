package com.notmiyouji.newsapp.java.Global;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.notmiyouji.newsapp.R;

public class NoInternetConnection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);
    }
}