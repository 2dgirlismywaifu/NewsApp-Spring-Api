package com.notmiyouji.newsapp.java.global.recycleviewadapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.RSSURL.LoadFollowType;

import java.util.HashMap;
import java.util.Map;

public class NewsTypeAdapter extends RecyclerView.Adapter<NewsTypeAdapter.NewsTypeHolder> {

    AppCompatActivity activity;
    String name;

    public NewsTypeAdapter(AppCompatActivity activity, String name) {
        this.activity = activity;
        this.name = name;
    }

    @NonNull
    @Override
    public NewsTypeAdapter.NewsTypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.news_type, parent, false);
        return new NewsTypeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsTypeAdapter.NewsTypeHolder holder, int position) {
        Map<String, String> data = newstype(activity);
        holder.news_type.setText(data.keySet().toArray()[position].toString());
        //Set acction for newsType Buttom
        holder.news_type.setOnClickListener(v -> {
            final ProgressDialog mDialog = new ProgressDialog(activity);
            mDialog.setMessage(activity.getString(R.string.loading_messeage));
            mDialog.show();
            //fetch follow category
            //get value from key HashMap
            String category = data.get(data.keySet().toArray()[position].toString());
            LoadFollowType loadFollowType = new LoadFollowType(activity, activity.findViewById(R.id.cardnews_view_vertical), mDialog, name);
            loadFollowType.startLoad(category);
        });
    }

    @Override
    public int getItemCount() {
        return newstype(activity).size();
    }


    public HashMap<String, String> newstype(AppCompatActivity activity) {
        HashMap<String, String> data = new HashMap<>();
        Context context = activity.getBaseContext();
        data.put(context.getString(R.string.breakingnews_type), "BreakingNews");
        data.put(context.getString(R.string.worldnews_type), "World");
        data.put(context.getString(R.string.news_type), "News");
        data.put(context.getString(R.string.sportnews_type), "Sport");
        data.put(context.getString(R.string.lawnews_type), "Law");
        data.put(context.getString(R.string.educationnews_type), "Education");
        data.put(context.getString(R.string.healthnews_type), "Health");
        data.put(context.getString(R.string.lifestylenews_type), "LifeStyle");
        data.put(context.getString(R.string.travelnews_type), "Travel");
        data.put(context.getString(R.string.sciencenews_type), "Science");
        return data;
    }

    public static class NewsTypeHolder extends RecyclerView.ViewHolder {
        Button news_type;

        public NewsTypeHolder(@NonNull View itemView) {
            super(itemView);
            news_type = itemView.findViewById(R.id.news_type_text);
        }
    }
}
