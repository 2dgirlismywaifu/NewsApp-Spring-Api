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

import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.RetrofitInterface.NewsAPPInterface;
import com.notmiyouji.newsapp.kotlin.SharedSettings.SaveUserLogined;
import com.notmiyouji.newsapp.kotlin.UpdateModel.CheckPassword;
import com.notmiyouji.newsapp.kotlin.UpdateModel.Password;

import retrofit2.Call;

public class PasswordController {
    String userid;
    String email;
    String oldpassword;
    String newpassword;
    String confirmpassword;
    AppCompatActivity activity;
    NewsAPPInterface newsAPPInterface = getAPIClient().create(NewsAPPInterface.class);
    public PasswordController(String userid, String email, String oldpassword, String newpassword, String confirmpassword, AppCompatActivity activity) {
        this.userid = userid;
        this.email = email;
        this.oldpassword = oldpassword;
        this.newpassword = newpassword;
        this.confirmpassword = confirmpassword;
        this.activity = activity;
    }

    public void CheckPassword() {
        //Retrofit call check old password request
        Call<CheckPassword> checkPassNow = newsAPPInterface.checkPasswordAccount(userid, email, oldpassword);
        checkPassNow.enqueue(new retrofit2.Callback<CheckPassword>() {
            @Override
            public void onResponse(Call<CheckPassword> call, retrofit2.Response<CheckPassword> response) {
                if (response.isSuccessful()) {
                    CheckPassword checkPassword = response.body();
                    if (checkPassword.getStatus().equals("pass")) {
                        UpdatePassword();
                    } else {
                        Toast.makeText(activity, R.string.oldpass_error, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckPassword> call, Throwable t) {

            }
        });
    }

    private void UpdatePassword() {
        //Retrofit call update password request
        Call<Password> updatePasswordNow = newsAPPInterface.updatePasswordAccount(userid,newpassword);
        updatePasswordNow.enqueue(new retrofit2.Callback<Password>() {
            @Override
            public void onResponse(Call<Password> call, retrofit2.Response<Password> response) {
                if (response.isSuccessful()) {
                    Password password = response.body();
                    if (password.getStatus().equals("pass")) {
                        Toast.makeText(activity, R.string.password_updated, Toast.LENGTH_SHORT).show();
                        //Update password in firebase
                        FirebaseAuth.getInstance().getCurrentUser().updatePassword(newpassword);
                        //Sign out
                        FirebaseAuth.getInstance().signOut();
                        SaveUserLogined saveUserLogined = new SaveUserLogined(activity);
                        saveUserLogined.saveUserLogined("", "", "", "", "", "","");
                        saveUserLogined.saveBirthday("");
                        saveUserLogined.saveGender("");
                        //Restart application
                        Toast.makeText(activity, R.string.sign_out_success, Toast.LENGTH_SHORT).show();
                        Intent intent = activity.getBaseContext().getPackageManager().getLaunchIntentForPackage(
                                activity.getBaseContext().getPackageName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent);
                    } else {
                        Toast.makeText(activity, R.string.password_updated_failed, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Password> call, Throwable t) {

            }
        });
    }

}
