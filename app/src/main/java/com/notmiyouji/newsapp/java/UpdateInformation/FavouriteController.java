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

package com.notmiyouji.newsapp.java.UpdateInformation;

import static com.notmiyouji.newsapp.java.Retrofit.NewsAPPAPI.getAPIClient;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.FavouriteModel.NewsFavourite;
import com.notmiyouji.newsapp.kotlin.FavouriteModel.NewsFavouriteCheck;
import com.notmiyouji.newsapp.kotlin.FavouriteModel.NewsUnfavourite;
import com.notmiyouji.newsapp.kotlin.RetrofitInterface.NewsAPPInterface;

import retrofit2.Call;
import retrofit2.Response;

public class FavouriteController {
    AppCompatActivity activity;
    NewsAPPInterface newsAPPInterface = getAPIClient().create(NewsAPPInterface.class);

    public FavouriteController(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void addFavouriteEmail(String userid, String url, String title, String imageurl, String pubdate, String sourcename) {
        Call<NewsFavourite> addFavouriteEmail = newsAPPInterface.accountNewsFavourite(userid, url, title, imageurl, pubdate, sourcename);
        addFavouriteEmail.enqueue(new retrofit2.Callback<NewsFavourite>() {
            @Override
            public void onResponse(Call<NewsFavourite> call, Response<NewsFavourite> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        Toast.makeText(activity, R.string.add_favourite, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsFavourite> call, Throwable t) {

            }
        });
    }

    public void removeFavouriteEmail(String userid, String url, String title, String imageurl, String sourcename) {
        Call<NewsUnfavourite> removeFavouriteEmail = newsAPPInterface.accountNewsUnfavourite(userid, url, title, imageurl, sourcename);
        removeFavouriteEmail.enqueue(new retrofit2.Callback<NewsUnfavourite>() {
            @Override
            public void onResponse(Call<NewsUnfavourite> call, Response<NewsUnfavourite> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        Toast.makeText(activity, R.string.remove_favourite, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsUnfavourite> call, Throwable t) {

            }
        });

    }

    public void checkFavouriteEmail(String userid, String url, String title, String imageurl, String sourcename,
            MenuItem favourite, MenuItem unfavourite) {
        Call<NewsFavouriteCheck> removeFavouriteEmail = newsAPPInterface.accountNewsfavouriteCheck(userid, url, title, imageurl, sourcename);
        removeFavouriteEmail.enqueue(new retrofit2.Callback<NewsFavouriteCheck>() {
            @Override
            public void onResponse(Call<NewsFavouriteCheck> call, Response<NewsFavouriteCheck> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        favourite.setVisible(false);
                        unfavourite.setVisible(true);
                    }
                    else {
                        favourite.setVisible(true);
                        unfavourite.setVisible(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsFavouriteCheck> call, Throwable t) {

            }
        });

    }

    public void checkFavouriteEmailRecycleView(String userid, String url, String title, String imageurl, String sourcename,
                                             ImageView favourite, ImageView unfavourite) {
        Call<NewsFavouriteCheck> removeFavouriteEmail = newsAPPInterface.accountNewsfavouriteCheck(userid, url, title, imageurl, sourcename);
        removeFavouriteEmail.enqueue(new retrofit2.Callback<NewsFavouriteCheck>() {
            @Override
            public void onResponse(Call<NewsFavouriteCheck> call, Response<NewsFavouriteCheck> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        favourite.setVisibility(View.GONE);
                        unfavourite.setVisibility(View.VISIBLE);
                    }
                    else {
                        favourite.setVisibility(View.VISIBLE);
                        unfavourite.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsFavouriteCheck> call, Throwable t) {

            }
        });

    }

    public void addFavouriteSSO(String userid, String url, String title, String imageurl, String pubdate, String sourcename) {
        Call<NewsFavourite> addFavouriteEmail = newsAPPInterface.ssoNewsFavourite(userid, url, title, imageurl, pubdate, sourcename);
        addFavouriteEmail.enqueue(new retrofit2.Callback<NewsFavourite>() {
            @Override
            public void onResponse(Call<NewsFavourite> call, Response<NewsFavourite> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        Toast.makeText(activity, R.string.add_favourite, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsFavourite> call, Throwable t) {

            }
        });
    }

    public void removeFavouriteSSO(String userid, String url, String title, String imageurl, String sourcename) {
        Call<NewsUnfavourite> removeFavouriteEmail = newsAPPInterface.ssoNewsUnfavourite(userid, url, title, imageurl, sourcename);
        removeFavouriteEmail.enqueue(new retrofit2.Callback<NewsUnfavourite>() {
            @Override
            public void onResponse(Call<NewsUnfavourite> call, Response<NewsUnfavourite> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        Toast.makeText(activity, R.string.remove_favourite, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsUnfavourite> call, Throwable t) {

            }
        });

    }

    public void checkFavouriteSSO(String userid, String url, String title, String imageurl, String sourcename,
                                  MenuItem favourite, MenuItem unfavourite) {
        Call<NewsFavouriteCheck> removeFavouriteEmail = newsAPPInterface.ssoNewsfavouriteCheck(userid, url, title, imageurl, sourcename);
        removeFavouriteEmail.enqueue(new retrofit2.Callback<NewsFavouriteCheck>() {
            @Override
            public void onResponse(Call<NewsFavouriteCheck> call, Response<NewsFavouriteCheck> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        favourite.setVisible(false);
                        unfavourite.setVisible(true);
                    }
                    else {
                        favourite.setVisible(true);
                        unfavourite.setVisible(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsFavouriteCheck> call, Throwable t) {

            }
        });

    }
    public void checkFavouriteSSORecycleView(String userid, String url, String title, String imageurl, String sourcename,
                                             ImageView favourite, ImageView unfavourite) {
        Call<NewsFavouriteCheck> removeFavouriteEmail = newsAPPInterface.ssoNewsfavouriteCheck(userid, url, title, imageurl, sourcename);
        removeFavouriteEmail.enqueue(new retrofit2.Callback<NewsFavouriteCheck>() {
            @Override
            public void onResponse(Call<NewsFavouriteCheck> call, Response<NewsFavouriteCheck> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        favourite.setVisibility(View.GONE);
                        unfavourite.setVisibility(View.VISIBLE);
                    }
                    else {
                        favourite.setVisibility(View.VISIBLE);
                        unfavourite.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsFavouriteCheck> call, Throwable t) {

            }
        });

    }

}
