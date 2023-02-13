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
import com.notmiyouji.newsapp.kotlin.UpdateModel.Gender;

import retrofit2.Call;

public class GenderController {
    String userId;
    String gender;
    AppCompatActivity activity;
    NewsAPPInterface newsAPPInterface = getAPIClient().create(NewsAPPInterface.class);

    public GenderController(String userId, String gender, AppCompatActivity activity) {
        this.userId = userId;
        this.gender = gender;
        this.activity = activity;
    }

    public void updateGender() {
        //Retrofit call update gender request
        Call<Gender> call = newsAPPInterface.updateGenderAccount(userId, gender);
        call.enqueue(new retrofit2.Callback<Gender>() {
            @Override
            public void onResponse(Call<Gender> call, retrofit2.Response<Gender> response) {
                if (response.isSuccessful()) {
                    Gender gender = response.body();
                    if (gender.getStatus().equals("pass")) {
                        //Save fullname to shared preference
                        SaveUserLogined saveUserLogined = new SaveUserLogined(activity);
                        saveUserLogined.saveGender(gender.getGender());
                        Toast.makeText(activity, R.string.gender_updated, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, R.string.gender_updated_failed, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Gender> call, Throwable t) {

            }
        });
    }

    public void updateGenderSSO() {
        //Retrofit call update gender request
        Call<Gender> call = newsAPPInterface.updateGenderSSO(userId, gender);
        call.enqueue(new retrofit2.Callback<Gender>() {
            @Override
            public void onResponse(Call<Gender> call, retrofit2.Response<Gender> response) {
                if (response.isSuccessful()) {
                    Gender gender = response.body();
                    if (gender.getStatus().equals("pass")) {
                        //Save fullname to shared preference
                        SaveUserLogined saveUserLogined = new SaveUserLogined(activity);
                        saveUserLogined.saveGender(gender.getGender());
                        Toast.makeText(activity, R.string.gender_updated, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, R.string.gender_updated_failed, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Gender> call, Throwable t) {

            }
        });

    }
}
