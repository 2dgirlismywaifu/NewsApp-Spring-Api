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
