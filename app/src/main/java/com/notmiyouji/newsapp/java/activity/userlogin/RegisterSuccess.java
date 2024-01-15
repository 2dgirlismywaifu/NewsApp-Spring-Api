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

import android.app.ActivityOptions;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.retrofit.NewsAppApi;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.NewsAppInterface;
import com.notmiyouji.newsapp.kotlin.model.RecoveryCode;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadFollowLanguageSystem;

import retrofit2.Call;

public class RegisterSuccess extends AppCompatActivity {
    private TextView recoveryCode;
    private final NewsAppInterface newsAPPInterface = NewsAppApi.getAPIClient().create(NewsAppInterface.class);

    protected void onCreate(android.os.Bundle savedInstanceState) {
        LoadFollowLanguageSystem loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_success);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        recoveryCode = findViewById(R.id.recovery_code_label);
        String email = getIntent().getStringExtra("email");
        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //Go back to login screen
        Button goBackLoginScreen = findViewById(R.id.GoBackLoginScreen);
        goBackLoginScreen.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //Show recovery code
        ShowRecoveryCode(email);
    }


    private void ShowRecoveryCode(String email) {
        Call<RecoveryCode> call = newsAPPInterface.getRecoveryCode(email);
        assert call != null;
        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RecoveryCode> call, @NonNull retrofit2.Response<RecoveryCode> response) {
                if (response.isSuccessful()) {
                    RecoveryCode recoveryCode = response.body();
                    if (recoveryCode != null) {
                        RegisterSuccess.this.recoveryCode.setText(recoveryCode.getRecovery());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecoveryCode> call, @NonNull Throwable t) {
            }
        });
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
        @Override
        public void handleOnBackPressed() {
            ActivityOptions.makeSceneTransitionAnimation(RegisterSuccess.this).toBundle();
            finish();
        }
    };

    public OnBackPressedCallback getCallback() {
        return callback;
    }
}
