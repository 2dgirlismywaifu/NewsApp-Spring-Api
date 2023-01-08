package com.notmiyouji.newsapp.java.global.recycleviewadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.sharedSettings.SharedPreferenceSettings;

import java.util.ArrayList;

public class WallpaperHeaderAdpater extends RecyclerView.Adapter<WallpaperHeaderAdpater.ViewHolder> {


    ArrayList<Integer> data = wallpaperList();
    AppCompatActivity activity;
    SharedPreferenceSettings sharedPreferenceSettings;

    public WallpaperHeaderAdpater(AppCompatActivity activity) {
        this.activity = activity;
        sharedPreferenceSettings = new SharedPreferenceSettings(activity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallpaper_header_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Integer path = data.get(position);
        holder.wallpaperHeader.setImageResource(path);
        holder.changeBtn.setOnClickListener(v -> {
            //Send Image Resource to SharedPreference
            sharedPreferenceSettings.getSharedWallpaperHeader(path);
            Toast.makeText(activity, R.string.change_wall_messeage, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public ArrayList<Integer> wallpaperList() {
        ArrayList<Integer> data = new ArrayList<>();
        data.add(R.drawable.anime_landscapes_background__1);
        data.add(R.drawable.anime_landscapes_background__2);
        data.add(R.drawable.anime_landscapes_background__3);
        data.add(R.drawable.anime_landscapes_background__4);
        data.add(R.drawable.anime_landscapes_background__5);
        data.add(R.drawable.anime_landscapes_background__6);
        data.add(R.drawable.anime_landscapes_background__7);
        data.add(R.drawable.anime_landscapes_background__8);
        data.add(R.drawable.anime_landscapes_background__9);
        data.add(R.drawable.anime_landscapes_background__10);
        data.add(R.drawable.anime_landscapes_background__11);
        data.add(R.drawable.anime_landscapes_background__12);
        data.add(R.drawable.anime_landscapes_background__13);
        data.add(R.drawable.anime_landscapes_background__14);
        data.add(R.drawable.anime_landscapes_background__15);
        data.add(R.drawable.anime_landscapes_background__16);
        data.add(R.drawable.anime_landscapes_background__17);
        data.add(R.drawable.anime_landscapes_background__18);
        data.add(R.drawable.anime_landscapes_background__19);
        data.add(R.drawable.anime_landscapes_background__20);
        return data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView wallpaperHeader;
        Button changeBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            wallpaperHeader = itemView.findViewById(R.id.imgView);
            changeBtn = itemView.findViewById(R.id.buttonWall);
        }
    }
}

