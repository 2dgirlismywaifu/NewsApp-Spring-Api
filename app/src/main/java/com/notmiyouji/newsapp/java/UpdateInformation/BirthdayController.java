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
import com.notmiyouji.newsapp.kotlin.RetrofitInterface.NewsAPPInterface;
import com.notmiyouji.newsapp.kotlin.SharedSettings.SaveUserLogined;
import com.notmiyouji.newsapp.kotlin.UpdateModel.Birthday;

import retrofit2.Call;

public class BirthdayController {
    String userId;
    String birthday;
    AppCompatActivity activity;
    NewsAPPInterface newsAPPInterface = getAPIClient().create(NewsAPPInterface.class);

    public BirthdayController(String userId, String birthday, AppCompatActivity activity) {
        this.userId = userId;
        this.birthday = birthday;
        this.activity = activity;
    }
    public void updateBirthday() {
        //Retrofit call update fullname request
        Call<Birthday> call = newsAPPInterface.updateBirthdayAccount(userId, birthday);
        call.enqueue(new retrofit2.Callback<Birthday>() {
            @Override
            public void onResponse(Call<Birthday> call, retrofit2.Response<Birthday> response) {
                if (response.isSuccessful()) {
                    Birthday birthday = response.body();
                    if (birthday.getStatus().equals("pass")) {
                        //Save fullname to shared preference
                        SaveUserLogined saveUserLogined = new SaveUserLogined(activity);
                        saveUserLogined.saveBirthday(birthday.getBirthday());
                        Toast.makeText(activity, R.string.birthday_updated, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, R.string.birthday_updated_failed, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Birthday> call, Throwable t) {

            }
        });
    }

    public void updateBirthdaySSO() {
        //Retrofit call update fullname request
        Call<Birthday> call = newsAPPInterface.updateBirthdaySSO(userId, birthday);
        call.enqueue(new retrofit2.Callback<Birthday>() {
            @Override
            public void onResponse(Call<Birthday> call, retrofit2.Response<Birthday> response) {
                if (response.isSuccessful()) {
                    Birthday birthday = response.body();
                    if (birthday.getStatus().equals("pass")) {
                        //Save fullname to shared preference
                        SaveUserLogined saveUserLogined = new SaveUserLogined(activity);
                        saveUserLogined.saveBirthday(birthday.getBirthday());
                        Toast.makeText(activity, R.string.birthday_updated, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, R.string.birthday_updated_failed, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Birthday> call, Throwable t) {

            }
        });

    }

}
