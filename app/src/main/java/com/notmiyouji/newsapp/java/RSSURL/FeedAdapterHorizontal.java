package com.notmiyouji.newsapp.java.RSSURL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;
import com.notmiyouji.newsapp.kotlin.RSSFeed.RSSObject;

public class FeedAdapterHorizontal extends RecyclerView.Adapter<FeedAdapterHorizontal.FeedViewHolder> {

    RSSObject rssObject;
    AppCompatActivity activity;

    private final LayoutInflater inflater;

    public FeedAdapterHorizontal(RSSObject rssObject, AppCompatActivity activity) {
        this.rssObject = rssObject;
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.news_items_horizontal,parent,false);
        return new FeedViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("RtlHardcoded")
    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {

        holder.txtTitle.setText(rssObject.getItems().get(position).getTitle());
        holder.txtPubDate.setText(rssObject.getItems().get(position).getPubDate());
        holder.txtsource.setText(rssObject.getItems().get(position).getLink());
//        try
//        {
//            String path = rssObject.getItems().get(position).getThumbnail();
//            Picasso.get().load(path).into(holder.imageView);
//        }
//        catch (IllegalArgumentException e)
//        {
//            e.printStackTrace();
//
//        }
        String path = rssObject.getItems().get(position).getThumbnail();
        LoadImageURL loadImageURL = new LoadImageURL(path,holder.imageView, holder);
        loadImageURL.getImageFromURL();
    }

    @Override
    public int getItemCount() {
        try
        {
            return rssObject.getItems().size();
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
            return 0;
        }

    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener
    {

        public TextView txtTitle,txtPubDate,txtsource;
        public ImageView imageView;
        public Activity activity;

        private ItemClickListener itemClickListener;

        public FeedViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPubDate = itemView.findViewById(R.id.txtPubDate);
            txtsource = itemView.findViewById(R.id.txtSource);
            imageView = itemView.findViewById(R.id.imgNews);
            //Set Event
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public interface ItemClickListener {
            void onClick(View view, int position, boolean isLongClick);
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
