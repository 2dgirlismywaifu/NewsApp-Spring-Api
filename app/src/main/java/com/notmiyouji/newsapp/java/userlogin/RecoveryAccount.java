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

package com.notmiyouji.newsapp.java.userlogin;

import static com.notmiyouji.newsapp.java.retrofit.NewsAppApi.getAPIClient;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;
import com.notmiyouji.newsapp.kotlin.NewsAppInterface;
import com.notmiyouji.newsapp.kotlin.gravatar.RequestImage;
import com.notmiyouji.newsapp.kotlin.model.UserInformation;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadFollowLanguageSystem;

import java.util.Objects;

import retrofit2.Call;


public class RecoveryAccount extends AppCompatActivity {
    LoadFollowLanguageSystem loadFollowLanguageSystem;
    TextInputEditText newPassword, confirmPassword;
    TextView fullName, username;
    String email, usernameIntent, fullname, userid, code;
    Button changePassword;
    ShapeableImageView avatar;
    NewsAppInterface newsAPPInterface = getAPIClient().create(NewsAppInterface.class);

    @SuppressLint("SetTextI18n")
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
        code = getIntent().getStringExtra("code");
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
            getOnBackPressedDispatcher().onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //Change Password Button
        changePassword = findViewById(R.id.ChangeButton);
        changePassword.setOnClickListener(v -> {
            String newPasswordString = String.valueOf(newPassword.getText());
            String confirmPasswordString = String.valueOf(confirmPassword.getText());
            if (newPasswordString.isEmpty() || confirmPasswordString.isEmpty()) {
                Toast.makeText(RecoveryAccount.this, R.string.password_empty, Toast.LENGTH_SHORT).show();
            } else if (!newPasswordString.equals(confirmPasswordString)) {
                Toast.makeText(RecoveryAccount.this, R.string.password_is_not_same, Toast.LENGTH_SHORT).show();
            } else {
                UpdatePassword(userid, email, newPasswordString, code);
            }
        });
    }

    private void UpdatePassword(String userid, String email, String newPass, String code) {
        //Retrofit call update password request
        Call<UserInformation> updatePasswordNow = newsAPPInterface.changeUserPassword(userid, email, "", newPass, code);
        assert updatePasswordNow != null;
        updatePasswordNow.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserInformation> call, @NonNull retrofit2.Response<UserInformation> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (Objects.equals(response.body().getStatus(), "success")) {
                        Toast.makeText(RecoveryAccount.this, R.string.password_updated, Toast.LENGTH_SHORT).show();
                        ActivityOptions.makeSceneTransitionAnimation(RecoveryAccount.this).toBundle();
                        finish();
                    } else {
                        Toast.makeText(RecoveryAccount.this, R.string.password_updated_failed, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserInformation> call, @NonNull Throwable t) {
                Toast.makeText(RecoveryAccount.this, R.string.password_updated_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }
    OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
        @Override
        public void handleOnBackPressed() {
            ActivityOptions.makeSceneTransitionAnimation(RecoveryAccount.this).toBundle();
            finish();
        }
    };

    public OnBackPressedCallback getCallback() {
        return callback;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
    }
}