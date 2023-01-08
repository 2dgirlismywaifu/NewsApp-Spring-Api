package com.notmiyouji.newsapp.java.global.recycleviewadapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.RSSURL.SourceNewsDetails;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;
import com.notmiyouji.newsapp.kotlin.RSSSource.NewsSource;

import java.util.List;

public class ListSourceAdapter extends RecyclerView.Adapter<ListSourceAdapter.ListSourceHolder>{

    AppCompatActivity activity;
    List<NewsSource> newsSourceList;

    public ListSourceAdapter(AppCompatActivity activity, List<NewsSource> newsSourceList) {
        this.activity = activity;
        this.newsSourceList = newsSourceList;
    }

    @NonNull
    @Override
    public ListSourceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.source_news_list, parent, false);
        return new ListSourceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListSourceHolder holder, int position) {
        NewsSource newsSource = newsSourceList.get(position);
        holder.source_name.setText(newsSource.getSource_name());
        holder.source_description.setText(newsSource.getSource_url());
        String path = newsSource.getImgae();
        LoadImageURL loadImageURL = new LoadImageURL(path);
        loadImageURL.getImageFromURL(holder.source_image, holder);
        //Picasso.get().load(path).into(holder.source_image);
        holder.detailsbtn.setOnClickListener(v -> {
            Intent intent = new Intent(activity, SourceNewsDetails.class);
            intent.putExtra("source_name", newsSource.getSource_name());
            intent.putExtra("source_url", newsSource.getSource_url());
            intent.putExtra("source_description", newsSource.getInformation());
            intent.putExtra("source_image", newsSource.getImgae());
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return newsSourceList.size();
    }

    public static class ListSourceHolder extends RecyclerView.ViewHolder {

        TextView source_name, source_description;
        ImageView source_image;
        Button detailsbtn;
        public ListSourceHolder(@NonNull View itemView) {
            super(itemView);
            source_name = itemView.findViewById(R.id.source_name);
            source_description = itemView.findViewById(R.id.source_description);
            source_image = itemView.findViewById(R.id.imgNews);
            detailsbtn = itemView.findViewById(R.id.button_details);
        }
    }
}

