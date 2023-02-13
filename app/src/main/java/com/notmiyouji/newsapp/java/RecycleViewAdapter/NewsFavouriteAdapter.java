package com.notmiyouji.newsapp.java.RecycleViewAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.UpdateInformation.FavouriteController;
import com.notmiyouji.newsapp.kotlin.FavouriteModel.NewsFavouriteShow;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;
import com.notmiyouji.newsapp.kotlin.OpenActivity.OpenNewsDetails;
import com.notmiyouji.newsapp.kotlin.RSSFeed.Category.ItemsCategory;
import com.notmiyouji.newsapp.kotlin.RegEXImage;
import com.notmiyouji.newsapp.kotlin.SharedSettings.GetUserLogined;

import java.util.List;

public class NewsFavouriteAdapter extends RecyclerView.Adapter<NewsFavouriteAdapter.FeedViewVerticalHolder> {
    private final LayoutInflater inflater;
    List<NewsFavouriteShow> items;
    AppCompatActivity activity;
    GetUserLogined getUserLogined;
    FavouriteController favouriteController;

    public NewsFavouriteAdapter(List<NewsFavouriteShow> items, AppCompatActivity activity) {
        this.items = items;
        this.activity = activity;
        getUserLogined = new GetUserLogined(activity);
        favouriteController = new FavouriteController(activity);
        inflater = LayoutInflater.from(activity);
    }

    @NonNull
    @Override
    public FeedViewVerticalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.news_items_vertical, parent, false);
        return new FeedViewVerticalHolder(itemView);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(FeedViewVerticalHolder holder, int position) {
        holder.txtTitle.setText(items.get(position).getTitle());
        holder.txtPubDate.setText(items.get(position).getPubdate());
        holder.txtsource.setText(items.get(position).getSourceName());
        String path = items.get(position).getImageUrl();
        LoadImageURL loadImageURL = new LoadImageURL(path);
        loadImageURL.getImageFromURL(holder.imageView, holder);
        switch (getUserLogined.getStatus()) {
            case "login":
                favouriteController.checkFavouriteEmailRecycleView(getUserLogined.getUserID(),
                        items.get(position).getUrl(),
                        items.get(position).getTitle(),
                        path, items.get(position).getSourceName(), holder.favouritebtn, holder.unfavouritebtn);
                break;
            case "google":
                favouriteController.checkFavouriteSSORecycleView(getUserLogined.getUserID(),
                        items.get(position).getUrl(),
                        items.get(position).getTitle(),
                        path, items.get(position).getSourceName(), holder.favouritebtn, holder.unfavouritebtn);
                break;
            default:
                holder.favouritebtn.setVisibility(View.VISIBLE);
                holder.unfavouritebtn.setVisibility(View.GONE);
                break;
        }
        holder.favouritebtn.setOnClickListener(v -> {
            switch (getUserLogined.getStatus()) {
                case "login":
                    favouriteController.addFavouriteEmail(getUserLogined.getUserID(),
                            items.get(position).getUrl(),
                            items.get(position).getTitle(),
                            path,items.get(position).getPubdate(),
                            items.get(position).getSourceName());
                    holder.favouritebtn.setVisibility(View.GONE);
                    holder.unfavouritebtn.setVisibility(View.VISIBLE);
                    break;
                case "google":
                    favouriteController.addFavouriteSSO(getUserLogined.getUserID(),
                            items.get(position).getUrl(),
                            items.get(position).getTitle(),
                            path,items.get(position).getPubdate(),
                            items.get(position).getSourceName());
                    holder.favouritebtn.setVisibility(View.GONE);
                    holder.unfavouritebtn.setVisibility(View.VISIBLE);
                    break;
                default:
                    Toast.makeText(activity, R.string.please_login_to_use_this_feature, Toast.LENGTH_SHORT).show();
                    break;
            }

        });
        holder.unfavouritebtn.setOnClickListener(v -> {
            switch (getUserLogined.getStatus()) {
                case "login":
                    favouriteController.removeFavouriteEmail(getUserLogined.getUserID(),
                            items.get(position).getUrl(),
                            items.get(position).getTitle(),
                            path, items.get(position).getSourceName());
                    holder.favouritebtn.setVisibility(View.VISIBLE);
                    holder.unfavouritebtn.setVisibility(View.GONE);
                    notifyDataSetChanged();
                    break;
                case "google":
                    favouriteController.removeFavouriteSSO(getUserLogined.getUserID(),
                            items.get(position).getUrl(),
                            items.get(position).getTitle(),
                            path, items.get(position).getSourceName());
                    holder.favouritebtn.setVisibility(View.VISIBLE);
                    holder.unfavouritebtn.setVisibility(View.GONE);
                    notifyDataSetChanged();
                    break;
                default:
                    Toast.makeText(activity, R.string.please_login_to_use_this_feature, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
        holder.itemView.setOnClickListener(v -> {
            String getpath;
            if (path != null) {
                getpath = path;
            }
            else {
                getpath = "https://techvccloud.mediacdn.vn/2018/3/29/notavailableen-1522298007107364895792-21-0-470-800-crop-152229801290023105615.png";
            }
            //Webview is cool but it's not the best way to show the news, so I'm going to use a Chrome Custom Tab
            //If your browser not installed, it will open in the webview
            OpenNewsDetails openNewsDetails = new OpenNewsDetails(
                    items.get(position).getUrl(),
                    items.get(position).getTitle(),
                    getpath,
                    items.get(position).getSourceName(),
                    items.get(position).getPubdate(), activity);
            openNewsDetails.openNewsDetails();
        });
    }

    @Override
    public int getItemCount() {
        try {
            return items.size();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void clear() {
        int size = items.size();
        items.clear();
        notifyItemRangeRemoved(0, size);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<NewsFavouriteShow> filterList) {
        this.items = filterList;
        notifyDataSetChanged();
    }

    public static class FeedViewVerticalHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle, txtPubDate, txtsource;
        public ImageView imageView, favouritebtn, unfavouritebtn;
        public Activity activity;

        public FeedViewVerticalHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPubDate = itemView.findViewById(R.id.txtPubDate);
            txtsource = itemView.findViewById(R.id.txtSource);
            imageView = itemView.findViewById(R.id.imgNews);
            favouritebtn = itemView.findViewById(R.id.favouriteBtn);
            unfavouritebtn = itemView.findViewById(R.id.unfavouriteBtn);
            //Set Event
        }
    }
}
