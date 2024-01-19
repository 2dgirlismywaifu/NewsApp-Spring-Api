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

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.notmiyouji.newsapp.R
import com.notmiyouji.newsapp.java.activity.HomePage
import com.notmiyouji.newsapp.kotlin.sharedsettings.SaveThemeSettings

class ThemeAdapter(var activity: AppCompatActivity) :
    RecyclerView.Adapter<ThemeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.radio_button_list, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = themeList(activity)
        holder.radioButton.text = data.keys.toTypedArray()[position]
        holder.radioButton.setOnClickListener {
            val defTheme = data[data.keys.toTypedArray()[position]]!!
            val saveThemeSettings = SaveThemeSettings(activity)
            saveThemeSettings.saveTheme(defTheme)
            Toast.makeText(activity, R.string.theme_change_messeage, Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, HomePage::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                activity.overrideActivityTransition(Activity.OVERRIDE_TRANSITION_OPEN,0, 0)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                activity.finish()
                activity.overrideActivityTransition(Activity.OVERRIDE_TRANSITION_CLOSE,0, 0)
                activity.startActivity(intent)
            } else {
                @Suppress("DEPRECATION")
                activity.overridePendingTransition(0, 0)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                activity.finish()
                @Suppress("DEPRECATION")
                activity.overridePendingTransition(0, 0)
                activity.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return themeList(activity).size
    }

    private fun themeList(activity: AppCompatActivity): HashMap<String, String> {
        val data = HashMap<String, String>()
        val context = activity.baseContext
        data[context.getString(R.string.follow_system)] = "system"
        data[context.getString(R.string.light_theme)] = "light"
        data[context.getString(R.string.dark_theme)] = "dark"
        return data
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var radioButton: RadioButton

        init {
            radioButton = itemView.findViewById(R.id.radio_button)
        }
    }
}