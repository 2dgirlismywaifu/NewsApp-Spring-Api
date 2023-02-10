package com.notmiyouji.newsapp.kotlin.RecycleViewAdapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.notmiyouji.newsapp.R
import com.notmiyouji.newsapp.java.RSSURL.HomePage
import com.notmiyouji.newsapp.kotlin.SharedSettings.SaveThemeSettings

class ThemeAdpater(var activity: AppCompatActivity) :
    RecyclerView.Adapter<ThemeAdpater.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.radio_button_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = themeList(activity)
        holder.radioButton.text = data.keys.toTypedArray()[position]
        holder.radioButton.setOnClickListener { v: View? ->
            val defTheme = data[data.keys.toTypedArray()[position]]!!
            val saveThemeSettings = SaveThemeSettings(activity)
            saveThemeSettings.saveTheme(defTheme)
            Toast.makeText(activity, R.string.theme_change_messeage, Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, HomePage::class.java)
            activity.overridePendingTransition(0, 0)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            activity.finish()
            activity.overridePendingTransition(0, 0)
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return themeList(activity).size
    }

    fun themeList(activity: AppCompatActivity): HashMap<String, String> {
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