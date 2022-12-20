package com.notmiyouji.newsapp.java.global.recycleviewadapter;

import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.RSSURL.LoadFollowType;

import java.util.ArrayList;

public class NewsTypeAdapter extends RecyclerView.Adapter<NewsTypeAdapter.NewsTypeHolder>{

    ArrayList<String> data = newstype();
    AppCompatActivity activity;


    public NewsTypeAdapter(AppCompatActivity activity) {
        this.activity = activity;
    }


    @NonNull
    @Override
    public NewsTypeAdapter.NewsTypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.news_type, parent, false);
        return new NewsTypeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsTypeAdapter.NewsTypeHolder holder, int position) {
        holder.news_type.setText(data.get(position));
        //Set acction for newsType Buttom
        holder.itemView.setOnClickListener( v -> {
            final ProgressDialog mDialog = new ProgressDialog(activity);
            mDialog.setMessage("Loading, please wait...");
            mDialog.show();
            //fetch follow category
            String category = holder.news_type.getText().toString();
            LoadFollowType loadFollowType = new LoadFollowType(activity, activity.findViewById(R.id.cardnews_view_vertical), mDialog);
            loadFollowType.startLoad(category);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public ArrayList<String> newstype() {
        ArrayList<String> data = new ArrayList<>();
        data.add("Breaking News");
        data.add("World");
        data.add("News");
        data.add("Sport");
        data.add("Law");
        data.add("Education");
        data.add("Health");
        data.add("Life Style");
        data.add("Travel");
        data.add("Science");
        return data;
    }

    public static class NewsTypeHolder extends RecyclerView.ViewHolder {
        TextView news_type;
        public NewsTypeHolder(@NonNull View itemView) {
            super(itemView);
            news_type = itemView.findViewById(R.id.news_type_text);
        }
    }
}
