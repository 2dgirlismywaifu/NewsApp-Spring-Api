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

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.retrofit.NewsAppApi;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.NewsAppInterface;
import com.notmiyouji.newsapp.kotlin.model.VerifyEmail;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.sharedsettings.SaveUserLogined;

import java.util.Objects;
import java.util.Timer;

import retrofit2.Call;

public class VerifyAccountForm extends AppCompatActivity {
    private LoadFollowLanguageSystem loadFollowLanguageSystem;
    private Button resendBtn;
    private final NewsAppInterface newsAPPInterface = NewsAppApi.getAPIClient().create(NewsAppInterface.class);
    private String userID, fullName, email, password, username;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_account_form);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        //Get string send from Sign Up form
        userID = getIntent().getStringExtra("user_id");
        fullName = getIntent().getStringExtra("fullname");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        username = getIntent().getStringExtra("username");
        TextView emailtext = findViewById(R.id.email_send);
        emailtext.setText(email);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //check if user is verified or not
        Button verifyBtn = findViewById(R.id.VerifiedButton);
        verifyBtn.setOnClickListener(v -> {
            //re-sign in user
            FirebaseAuth authVerifed = FirebaseAuth.getInstance();
            authVerifed.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser userVerifed = authVerifed.getCurrentUser();
                    assert userVerifed != null;
                    if (userVerifed.isEmailVerified()) {
                        //if user is verified, change verified status to true
                        updateStatus(email);
                        //After that, save user account to shared preferences
                        SaveUserLogined saveUserLogined = new SaveUserLogined(this);
                        saveUserLogined.saveUserLogined(userID, fullName, email, password, username, "not_available", "true");
                        //go to Register Success form
                        Intent intent = new Intent(this, RegisterSuccess.class);
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, R.string.please_verify_your_email_first_with_link_sent_to_your_mailbox, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        });
        //Back to Sign In form
        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle(R.string.verify_account);
            builder.setMessage(R.string.your_account_is_not_verified_yet_are_you_sure_to_go_back);
            builder.setPositiveButton(R.string.yes, (dialog, which) -> {
                getOnBackPressedDispatcher().onBackPressed();
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
                finish();
            });
            builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
            builder.show();
        });
        //resend verification email
        resendBtn = findViewById(R.id.ResendCodeBtn);
        resendBtn.setOnClickListener(v -> {
            resendBtn.setEnabled(false);
            //send verification email and wait 60 second to send again
            assert user != null;
            user.sendEmailVerification().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "NewsApp");
                    builder.setSmallIcon(R.mipmap.ic_launcher);
                    builder.setContentTitle(getString(R.string.verify_account));
                    builder.setContentText(getString(R.string.verification_email_sent_to_your_mailbox));
                    builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
                    notificationManagerCompat.notify(1, builder.build());
                    Toast.makeText(this, R.string.verification_email_sent_to_your_mailbox, Toast.LENGTH_SHORT).show();
                    Timer timer = new Timer();
                    timer.schedule(new java.util.TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(() -> resendBtn.setEnabled(true));
                        }
                    }, 300);
                } else {
                    Toast.makeText(this, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
                    resendBtn.setEnabled(true);
                }
            });

        });
    }

    //Replace deprecated onBackPressed() method
    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(VerifyAccountForm.this);
            builder.setTitle(R.string.verify_account);
            builder.setMessage(R.string.your_account_is_not_verified_yet_are_you_sure_to_go_back);
            builder.setPositiveButton(R.string.yes, (dialog, which) -> {
                ActivityOptions.makeSceneTransitionAnimation(VerifyAccountForm.this).toBundle();
                finish();
            });
            builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
            builder.show();
        }
    };

    public OnBackPressedCallback getCallback() {
        return callback;
    }

    private void updateStatus(String email) {
        Call<VerifyEmail> call = newsAPPInterface.verifyEmail(email);
        assert call != null;
        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<VerifyEmail> call, @NonNull retrofit2.Response<VerifyEmail> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (Objects.equals(response.body().getStatus(), "success")) {
                        Toast.makeText(VerifyAccountForm.this, "Account Verified Success", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<VerifyEmail> call, @NonNull Throwable t) {
                Toast.makeText(VerifyAccountForm.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onResume() {
        super.onResume();
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
    }
}