package com.notmiyouji.newsapp.java.global.recycleviewadapter.newsapi;

import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.NewsAPI.LoadFollowCategory;

import java.util.ArrayList;

public class NewsAPITypeAdapter extends RecyclerView.Adapter<NewsAPITypeAdapter.NewsTypeHolder>{

    ArrayList<String> data = newstype();
    AppCompatActivity activity;
    LoadFollowCategory loadFollowCategory = new LoadFollowCategory();

    public NewsAPITypeAdapter(AppCompatActivity activity) {
        this.activity = activity;
    }


    @NonNull
    @Override
    public NewsAPITypeAdapter.NewsTypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.news_type, parent, false);
        return new NewsTypeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAPITypeAdapter.NewsTypeHolder holder, int position) {
        holder.news_type.setText(data.get(position));
        holder.itemView.setOnClickListener(v -> {
            //load Dialog
            final ProgressDialog mDialog = new ProgressDialog(activity);
            mDialog.setMessage("Loading, please wait...");
            mDialog.show();
            //fetch follow category
            String category = holder.news_type.getText().toString();
            loadFollowCategory.LoadJSONCategory(activity, mDialog, category, activity.findViewById(R.id.cardnews_view_vertical));
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public ArrayList<String> newstype() {
        ArrayList<String> data = new ArrayList<>();
        data.add("General");
        data.add("Business");
        data.add("Entertainment");
        data.add("Health");
        data.add("Science");
        data.add("Sports");
        data.add("Technology");
        return data;
    }

    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }

    public static class NewsTypeHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        TextView news_type;
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
