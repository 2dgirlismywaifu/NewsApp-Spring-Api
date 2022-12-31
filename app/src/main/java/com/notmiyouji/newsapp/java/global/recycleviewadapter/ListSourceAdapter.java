package com.notmiyouji.newsapp.java.global.recycleviewadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.global.AzureSQLServer;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ListSourceAdapter extends RecyclerView.Adapter<ListSourceAdapter.ListSourceHolder>{

    AzureSQLServer azureSQLServer = new AzureSQLServer();
    PreparedStatement ps;
    AppCompatActivity activity;
    public final String READ = "SELECT NEWS_SOURCE.source_id, source_name, information, IMAGE_INFORMATION.[image]  FROM NEWS_SOURCE, IMAGE_INFORMATION WHERE NEWS_SOURCE.source_id = IMAGE_INFORMATION.source_id";

    public ListSourceAdapter(AppCompatActivity activity) {
        this.activity = activity;
    }

    public String getREAD() {
        return READ;
    }

    @NonNull
    @Override
    public ListSourceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.source_news_list, parent, false);
        return new ListSourceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListSourceHolder holder, int position) {
        try {
            JSONObject jsonObject = sourecs().getJSONObject(position);
            holder.source_name.setText(jsonObject.getString("source_name"));
            holder.source_description.setText(jsonObject.getString("information"));
            String path = jsonObject.getString("image");
            LoadImageURL loadImageURL = new LoadImageURL(path);
            loadImageURL.getImageFromURL(holder.source_image, holder);
            //Picasso.get().load(path).into(holder.source_image);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return sourecs().length();
    }

    //Get sources with JSONArray
    public JSONArray sourecs() {
        JSONArray jsonArray = new JSONArray();
        try {
            Connection con = azureSQLServer.getConnection();
            ps = con.prepareStatement(getREAD());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id", rs.getString(1));
                    jsonObject.put("source_name", rs.getString(2));
                    jsonObject.put("information", rs.getString(3));
                    jsonObject.put("image", rs.getString(4));
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            rs.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }


    public static class ListSourceHolder extends RecyclerView.ViewHolder {

        TextView source_name, source_description;
        ImageView source_image;

        public ListSourceHolder(@NonNull View itemView) {
            super(itemView);
            source_name = itemView.findViewById(R.id.source_name);
            source_description = itemView.findViewById(R.id.source_description);
            source_image = itemView.findViewById(R.id.imgNews);
        }
    }
}

