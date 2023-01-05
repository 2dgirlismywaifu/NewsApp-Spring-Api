package com.notmiyouji.newsapp.java.global;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.imageview.ShapeableImageView;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;

public class AboutApplication extends AppCompatActivity {

    ShapeableImageView imageView;
    RelativeLayout githubbtn, twitterbtn;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_application);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        imageView = findViewById(R.id.owner_avatar);
        //load image github owner
        String path = "https://avatars.githubusercontent.com/u/59259855?s=400&u=e458277f4cca06aeb82adcd83641b8c92008947c&v=4"; //my avater github
        LoadImageURL loadImageURL = new LoadImageURL(path);
        loadImageURL.loadImageOwner(imageView);
        //back button
        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //Go to GitHub Profile
        githubbtn = findViewById(R.id.linearGithub);
        githubbtn.setOnClickListener(v -> {
            intent = new Intent (Intent.ACTION_VIEW , Uri.parse("https://github.com/2dgirlismywaifu"));
            startActivity(intent);
        });
        //Go to Twitter Profile
        twitterbtn = findViewById(R.id.linearTwitter);
        twitterbtn.setOnClickListener(v -> {
            intent = new Intent (Intent.ACTION_VIEW , Uri.parse("https://twitter.com/MyWaifuis2DGirl"));
            startActivity(intent);
        });
    }
    public void onBackPressed() {
        super.onBackPressed();
        ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        finish();
    }
}