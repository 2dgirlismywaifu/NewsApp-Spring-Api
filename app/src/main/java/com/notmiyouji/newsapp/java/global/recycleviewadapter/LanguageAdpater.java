package com.notmiyouji.newsapp.java.global.recycleviewadapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.ConfigurationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.RSSURL.HomePage;
import com.notmiyouji.newsapp.java.global.LanguagePrefManager;
import com.notmiyouji.newsapp.kotlin.sharedSettings.SharedPreferenceSettings;

import java.util.HashMap;
import java.util.Locale;

public class LanguageAdpater extends RecyclerView.Adapter<LanguageAdpater.ViewHolder> {

    AppCompatActivity activity;

    public LanguageAdpater(AppCompatActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public LanguageAdpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.radio_button_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageAdpater.ViewHolder holder, int position) {
        HashMap<String, String> data = languageList(activity);
        holder.radioButton.setText(data.keySet().toArray()[position].toString());
        holder.radioButton.setOnClickListener(v -> {
            String defLang = data.get(data.keySet().toArray()[position].toString());
            assert defLang != null;
            LanguagePrefManager languagePrefManager = new LanguagePrefManager(activity);
            languagePrefManager.setLocal(defLang);
            Toast.makeText(activity, R.string.language_change_messeage, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity, HomePage.class);
            activity.overridePendingTransition(0, 0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.finish();
            activity.overridePendingTransition(0, 0);
            activity.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return languageList(activity).size();
    }

    public HashMap<String, String> languageList(AppCompatActivity activity) {
        HashMap<String, String> data = new HashMap<>();
        Context context = activity.getBaseContext();
        data.put(context.getString(R.string.english_language), "en");
        data.put(context.getString(R.string.vietnam_language), "vi");
        return data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radio_button);
        }
    }
}

