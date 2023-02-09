package com.notmiyouji.newsapp.kotlin

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.imageview.ShapeableImageView
import com.notmiyouji.newsapp.R

class LoadImageURL(
    private var url: String?,

    ) {
    fun getImageFromURL(
        imageView: ImageView?, viewHolder: RecyclerView.ViewHolder?
    ) {
        viewHolder?.itemView?.context?.let {
            imageView?.load(url) {
                crossfade(true)
                error(R.drawable.lungcu_hban)
            }
        }
    }

    //load image github owner
    fun loadImageOwner(imageView: ShapeableImageView) {
        imageView.load(url) {
            crossfade(true)
            error(R.drawable.user_192)
        }
    }
    fun loadImageUser(imageView: ShapeableImageView) {
        imageView.load(url) {
            crossfade(true)
            error(R.drawable.user_192)
        }
    }
    fun loadImageWelcome(imageView: ShapeableImageView) {
        imageView.load(url) {
            crossfade(true)
            error(R.drawable.anime_landscapes_background__11)
        }
    }
    fun loadImageforNewsDetails(imageView: ImageView) {
        imageView.load(url) {
            crossfade(true)
            error(R.drawable.not_available)
        }
    }
}