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

package com.notmiyouji.newsapp.java.Signed;

import static com.notmiyouji.newsapp.java.Retrofit.NewsAPPAPI.getAPIClient;

import android.app.ActivityOptions;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.Gravatar.RequestImage;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;
import com.notmiyouji.newsapp.kotlin.RetrofitInterface.NewsAPPInterface;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.UpdateModel.Password;

import retrofit2.Call;

public class RecoveryAccount extends AppCompatActivity {
    LoadFollowLanguageSystem loadFollowLanguageSystem;
    TextInputEditText newPassword, confirmPassword;
    TextView fullName, username;
    String email, usernameIntent, fullname, userid;
    Button changePassword;
    ShapeableImageView avatar;
    NewsAPPInterface newsAPPInterface = getAPIClient().create(NewsAPPInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_account);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();

        email = getIntent().getStringExtra("email");
        usernameIntent = getIntent().getStringExtra("username");
        fullname = getIntent().getStringExtra("fullname");
        userid = getIntent().getStringExtra("userid");
        fullName = findViewById(R.id.fullname);
        username = findViewById(R.id.username);
        fullName.setText(fullname);
        username.setText("@" + usernameIntent);
        //Password Input

        newPassword = findViewById(R.id.newspass_user_input);
        confirmPassword = findViewById(R.id.confirmpass_input);
        //Load avatar
        avatar = findViewById(R.id.avatar_user_logined);
        String avatarURL = new RequestImage(email).getGravatarURL();
        LoadImageURL loadImageURL = new LoadImageURL(avatarURL);
        loadImageURL.loadImageUser(avatar);
        //back button
        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //Change Password Button
        changePassword = findViewById(R.id.ChangeButton);
        changePassword.setOnClickListener(v -> {
            String newPasswordString = newPassword.getText().toString();
            String confirmPasswordString = confirmPassword.getText().toString();
            if (newPasswordString.isEmpty() || confirmPasswordString.isEmpty()) {
                Toast.makeText(RecoveryAccount.this, R.string.password_empty, Toast.LENGTH_SHORT).show();
            } else if (!newPasswordString.equals(confirmPasswordString)) {
                Toast.makeText(RecoveryAccount.this, R.string.password_is_not_same, Toast.LENGTH_SHORT).show();
            } else {
                UpdatePassword(userid, newPasswordString);
            }
        });
    }

    private void UpdatePassword(String userid, String newpassword) {
        //Retrofit call update password request
        Call<Password> updatePasswordNow = newsAPPInterface.updatePasswordAccount(userid,newpassword);
        updatePasswordNow.enqueue(new retrofit2.Callback<Password>() {
            @Override
            public void onResponse(Call<Password> call, retrofit2.Response<Password> response) {
                if (response.isSuccessful()) {
                    Password password = response.body();
                    if (password.getStatus().equals("pass")) {
                        Toast.makeText(RecoveryAccount.this, R.string.password_updated, Toast.LENGTH_SHORT).show();
                        ActivityOptions.makeSceneTransitionAnimation(RecoveryAccount.this).toBundle();
                        finish();
                    } else {
                        Toast.makeText(RecoveryAccount.this, R.string.password_updated_failed, Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<Password> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        finish();
    }
    @Override
    public void onResume() {
        super.onResume();
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
    }
}