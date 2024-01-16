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

package com.notmiyouji.newsapp.java.activity.userlogin;

import static com.notmiyouji.newsapp.java.retrofit.NewsAppApi.getAPIClient;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.NewsAppInterface;
import com.notmiyouji.newsapp.kotlin.Utils;
import com.notmiyouji.newsapp.kotlin.model.NewsFavourite;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

public class NewsFavouriteByUser {
    private final AppCompatActivity activity;
    private final NewsAppInterface newsAPPInterface = getAPIClient().create(NewsAppInterface.class);

    public NewsFavouriteByUser(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void addFavouriteByUser(String userId, String url, String title, String imageUrl, String pubDate, String sourceName) {
        if (imageUrl == null) {
            imageUrl = "not_found";
        }
        Call<NewsFavourite> addFavouriteEmail = newsAPPInterface.saveNewsFavouriteByUser(
                Utils.encodeToBase64(userId), Utils.encodeToBase64(url),
                Utils.encodeToBase64(title), Utils.encodeToBase64(imageUrl),
                Utils.encodeToBase64(pubDate));
        assert addFavouriteEmail != null;
        addFavouriteEmail.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<NewsFavourite> call, @NonNull Response<NewsFavourite> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(activity, R.string.add_favourite, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsFavourite> call, @NonNull Throwable t) {
                Toast.makeText(activity, R.string.add_favourite_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void removeFavouriteByUser(String userId, String favouriteId, String title) {
        Call<NewsFavourite> removeFavouriteEmail = newsAPPInterface.deleteNewsFavouriteByUser(
                Utils.encodeToBase64(userId), Utils.encodeToBase64(favouriteId), Utils.encodeToBase64(title));
        assert removeFavouriteEmail != null;
        removeFavouriteEmail.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<NewsFavourite> call, @NonNull Response<NewsFavourite> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(activity, R.string.remove_favourite, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsFavourite> call, @NonNull Throwable t) {
                Toast.makeText(activity, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void checkFavouriteNewsByUser(String userId, String title, MenuItem favourite, MenuItem unFavourite) {
        Call<NewsFavourite> checkFavouriteEmail = newsAPPInterface.accountCheckNewsFavourite(
                Utils.encodeToBase64(userId), Utils.encodeToBase64(title));
        assert checkFavouriteEmail != null;
        checkFavouriteEmail.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<NewsFavourite> call, @NonNull Response<NewsFavourite> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (Objects.equals(response.body().getStatus(), "found")) {
                            favourite.setVisible(false);
                            unFavourite.setVisible(true);
                        } else {
                            favourite.setVisible(true);
                            unFavourite.setVisible(false);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsFavourite> call, @NonNull Throwable t) {

            }
        });
    }

    public void checkFavouriteNewsInRecycleViewByUser(String userId, String title, ImageView favourite, ImageView unFavourite) {
        Call<NewsFavourite> checkFavouriteEmail = newsAPPInterface.accountCheckNewsFavourite(
                Utils.encodeToBase64(userId), Utils.encodeToBase64(title));
        assert checkFavouriteEmail != null;
        checkFavouriteEmail.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<NewsFavourite> call, @NonNull Response<NewsFavourite> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (Objects.equals(response.body().getStatus(), "found")) {
                            favourite.setVisibility(ImageView.GONE);
                            unFavourite.setVisibility(ImageView.VISIBLE);
                        } else {
                            favourite.setVisibility(ImageView.VISIBLE);
                            unFavourite.setVisibility(ImageView.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsFavourite> call, @NonNull Throwable t) {

            }
        });
    }
}
