/*
 * Copyright By @2dgirlismywaifu (2023) .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.notmiyouji.newsapp.kotlin.recycleview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.notmiyouji.newsapp.R
import com.notmiyouji.newsapp.kotlin.sharedsettings.SharedPreferenceSettings

class WallpaperHeaderAdpater(var activity: AppCompatActivity) :
    RecyclerView.Adapter<WallpaperHeaderAdpater.ViewHolder>() {
    var data = wallpaperList()
    private var sharedPreferenceSettings: SharedPreferenceSettings = SharedPreferenceSettings(activity)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.wallpaper_header_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val path = data[position]
        holder.wallpaperHeader.setImageResource(path)
        holder.changeBtn.setOnClickListener {
            //Send Image Resource to SharedPreference
            sharedPreferenceSettings.getSharedWallpaperHeader(path)
            Toast.makeText(activity, R.string.change_wall_messeage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun wallpaperList(): ArrayList<Int> {
        val data = ArrayList<Int>()
        data.add(R.drawable.anime_landscapes_background__1)
        data.add(R.drawable.anime_landscapes_background__2)
        data.add(R.drawable.anime_landscapes_background__3)
        data.add(R.drawable.anime_landscapes_background__4)
        data.add(R.drawable.anime_landscapes_background__5)
        data.add(R.drawable.anime_landscapes_background__6)
        data.add(R.drawable.anime_landscapes_background__7)
        data.add(R.drawable.anime_landscapes_background__8)
        data.add(R.drawable.anime_landscapes_background__9)
        data.add(R.drawable.anime_landscapes_background__10)
        data.add(R.drawable.anime_landscapes_background__11)
        data.add(R.drawable.anime_landscapes_background__12)
        data.add(R.drawable.anime_landscapes_background__13)
        data.add(R.drawable.anime_landscapes_background__14)
        data.add(R.drawable.anime_landscapes_background__15)
        data.add(R.drawable.anime_landscapes_background__16)
        data.add(R.drawable.anime_landscapes_background__17)
        data.add(R.drawable.anime_landscapes_background__18)
        data.add(R.drawable.anime_landscapes_background__19)
        data.add(R.drawable.anime_landscapes_background__20)
        return data
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var wallpaperHeader: ImageView
        var changeBtn: Button

        init {
            wallpaperHeader = itemView.findViewById(R.id.imgView)
            changeBtn = itemView.findViewById(R.id.buttonWall)
        }
    }
}