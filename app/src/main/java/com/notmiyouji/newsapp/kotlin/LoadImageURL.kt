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