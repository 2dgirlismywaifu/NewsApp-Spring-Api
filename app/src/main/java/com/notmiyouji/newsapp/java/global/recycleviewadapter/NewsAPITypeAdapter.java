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
import com.notmiyouji.newsapp.java.NewsAPI.LoadFollowCategory;

import java.util.HashMap;

public class NewsAPITypeAdapter extends RecyclerView.Adapter<NewsAPITypeAdapter.NewsTypeHolder>{
    private final AppCompatActivity activity;
    private final LoadFollowCategory loadFollowCategory = new LoadFollowCategory();
    private final String country;

    public NewsAPITypeAdapter(AppCompatActivity activity, String country) {
        this.activity = activity;
        this.country = country;
    }
    @NonNull
    @Override
    public NewsAPITypeAdapter.NewsTypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.news_type, parent, false);
        return new NewsTypeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAPITypeAdapter.NewsTypeHolder holder, int position) {
        HashMap<String, String> data = newstype(activity);
        holder.news_type.setText(data.keySet().toArray()[position].toString());
        holder.news_type.setOnClickListener(v -> {
            //load Dialog
            final ProgressDialog mDialog = new ProgressDialog(activity);
            mDialog.setMessage("Loading, please wait...");
            mDialog.show();
            //fetch follow category
            String category = data.get(data.keySet().toArray()[position].toString());
            loadFollowCategory.LoadJSONCategory(activity, mDialog, category, activity.findViewById(R.id.cardnews_view_vertical), country);
        });
    }

    @Override
    public int getItemCount() {
        return newstype(activity).size();
    }

    public HashMap<String, String> newstype(AppCompatActivity activity) {
        HashMap<String, String> data = new HashMap<>();
        Context context = activity.getBaseContext();
        data.put(context.getString(R.string.general_newsapi), "General");
        data.put(context.getString(R.string.business_newsapi), "Business");
        data.put(context.getString(R.string.entertainment_newsapi), "Entertainment");
        data.put(context.getString(R.string.health_newsapi), "Health");
        data.put(context.getString(R.string.science_newsapi), "Science");
        data.put(context.getString(R.string.sports_newsapi), "Sports");
        data.put(context.getString(R.string.technology_newsapi), "Technology");
        return data;
    }

    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }

    public static class NewsTypeHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        Button news_type;
        private ItemClickListener itemClickListener;
        public NewsTypeHolder(@NonNull View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);
            news_type = itemView.findViewById(R.id.news_type_text);
            //Set Event
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }
        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),true);
            return true;
        }
    }
}
