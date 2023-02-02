package com.notmiyouji.newsapp.java.Global;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.notmiyouji.newsapp.R;

public class MaterialAltertLoading {
    AppCompatActivity activity;
    MaterialAlertDialogBuilder builder;
    public MaterialAltertLoading(AppCompatActivity activity) {
        this.activity = activity;
    }
    public MaterialAlertDialogBuilder getDiaglog() {
        if (builder == null) {
            builder = new MaterialAlertDialogBuilder(activity);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle(activity.getString(R.string.app_name));
            //builder.setCancelable(false);
            builder.setView(R.layout.show_loading_bar);
        }
        return builder;
    }
}
