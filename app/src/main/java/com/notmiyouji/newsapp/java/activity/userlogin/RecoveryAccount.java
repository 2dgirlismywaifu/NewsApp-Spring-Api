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

import static com.notmiyouji.newsapp.kotlin.retrofit.NewsAppApi.getApiClient;

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
import com.google.firebase.auth.FirebaseAuth;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.util.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.util.LoadUrlImage;
import com.notmiyouji.newsapp.kotlin.retrofit.NewsAppInterface;
import com.notmiyouji.newsapp.kotlin.util.GravatarImage;
import com.notmiyouji.newsapp.kotlin.model.user.UserInformation;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadFollowLanguageSystem;

import java.util.Objects;

import retrofit2.Call;


public class RecoveryAccount extends AppCompatActivity {
    private LoadFollowLanguageSystem loadFollowLanguageSystem;
    private String email;
    private String userid;
    private final NewsAppInterface newsAPPInterface = getApiClient().create(NewsAppInterface.class);

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
        String usernameIntent = getIntent().getStringExtra("username");
        String fullname = getIntent().getStringExtra("fullname");
        userid = getIntent().getStringExtra("userid");
        TextView fullName = findViewById(R.id.fullname);
        TextView username = findViewById(R.id.username);
        fullName.setText(fullname);
        username.setText("@" + usernameIntent);
        //Found email
        TextView foundEmail = findViewById(R.id.found_email);
        foundEmail.setText(email);
        //Load avatar
        ShapeableImageView avatar = findViewById(R.id.avatar_user_logined);
        String avatarURL = new GravatarImage(email).getGravatarURL();
        LoadUrlImage loadUrlImage = new LoadUrlImage(avatarURL);
        loadUrlImage.loadImageUser(avatar);
        //back button
        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //Change Password Button
        Button changePassword = findViewById(R.id.ChangeButton);
        changePassword.setOnClickListener(v -> {
            UpdatePassword(userid, email);
        });
    }

    private void UpdatePassword(String userid, String email) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //Retrofit call update password request
                Call<UserInformation> updatePasswordNow = newsAPPInterface.changeUserToken(userid, email, firebaseAuth.getUid());
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
            } else {
                Toast.makeText(RecoveryAccount.this, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
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