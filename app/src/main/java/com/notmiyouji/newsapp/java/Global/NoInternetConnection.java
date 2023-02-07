package com.notmiyouji.newsapp.java.Global;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.notmiyouji.newsapp.R;

public class NoInternetConnection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);
    }
}