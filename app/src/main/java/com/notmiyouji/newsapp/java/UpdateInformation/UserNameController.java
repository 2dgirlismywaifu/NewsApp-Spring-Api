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

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.LoginedModel.CheckNickName;
import com.notmiyouji.newsapp.kotlin.RetrofitInterface.NewsAPPInterface;
import com.notmiyouji.newsapp.kotlin.SharedSettings.SaveUserLogined;
import com.notmiyouji.newsapp.kotlin.UpdateModel.UserName;

import retrofit2.Call;
import retrofit2.Response;

public class UserNameController {
    String userId;
    String userName;
    String email;
    AppCompatActivity activity;
    NewsAPPInterface newsAPPInterface = getAPIClient().create(NewsAPPInterface.class);

    public UserNameController(String userId, String userName, String email, AppCompatActivity activity) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.activity = activity;
    }


    public void checkUserName() {
        Call<CheckNickName> call = newsAPPInterface.checkNickname(userName, email);
        call.enqueue(new retrofit2.Callback<CheckNickName>() {
            @Override
            public void onResponse(Call<CheckNickName> call, Response<CheckNickName> response) {
                if (response.body().getNickname().equals(userName)) {
                    Toast.makeText(activity, R.string.username_already_exists, Toast.LENGTH_SHORT).show();
                } else {
                    updateUserName();
                }
            }

            @Override
            public void onFailure(Call<CheckNickName> call, Throwable t) {
            }
        });
    }
    public void updateUserName() {
        //Retrofit call update username request
        Call<UserName> call = newsAPPInterface.updateUserNameAccount(userId, userName);
        call.enqueue(new retrofit2.Callback<UserName>() {
            @Override
            public void onResponse(Call<UserName> call, retrofit2.Response<UserName> response) {
                if (response.isSuccessful()) {
                    UserName userName = response.body();
                    if (userName.getStatus().equals("pass")) {
                        SaveUserLogined saveUserLogined = new SaveUserLogined(activity);
                        saveUserLogined.saveUsername(userName.getUsername());
                        Toast.makeText(activity, R.string.username_updated, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, R.string.username_already_exists, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserName> call, Throwable t) {

            }
        });

    }
}
