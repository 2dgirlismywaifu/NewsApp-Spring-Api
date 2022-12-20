package com.notmiyouji.newsapp.kotlin

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.notmiyouji.newsapp.R


class LoadImageURL(


    private var url: String?,
    var imageView: ImageView?,
    private var viewHolder: RecyclerView.ViewHolder?
) {

    fun getImageFromURL() {
        viewHolder?.itemView?.context?.let {
            imageView?.load(url) {
                crossfade(true)
                error(R.drawable.not_available)
            }
        }
    }
}