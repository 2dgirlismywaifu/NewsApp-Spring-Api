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

import android.app.ActivityOptions;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.Retrofit.NewsAPPAPI;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.LoginedModel.Recovery;
import com.notmiyouji.newsapp.kotlin.LoginedModel.SignIn;
import com.notmiyouji.newsapp.kotlin.RetrofitInterface.NewsAPPInterface;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.SharedSettings.SaveUserLogined;

import retrofit2.Call;

public class RegisterSuccess extends AppCompatActivity {
    LoadFollowLanguageSystem loadFollowLanguageSystem;
    TextView recoveryCode;
    Button signinbtn, gobackLoginScreen;
    NewsAPPInterface newsAPPInterface = NewsAPPAPI.getAPIClient().create(NewsAPPInterface.class);

    protected void onCreate(android.os.Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_success);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        recoveryCode = findViewById(R.id.recovery_code_label);
        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");
        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //Go back to login screen
        gobackLoginScreen = findViewById(R.id.GoBackLoginScreen);
        gobackLoginScreen.setOnClickListener(v -> {
            onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //Show recovery code
        ShowRecoveryCode(email);
    }


    private void ShowRecoveryCode(String email) {
        Call<Recovery> call = newsAPPInterface.recoveryCode(email);
        call.enqueue(new retrofit2.Callback<Recovery>() {
            @Override
            public void onResponse(Call<Recovery> call, retrofit2.Response<Recovery> response) {
                if (response.isSuccessful()) {
                    Recovery recovery = response.body();
                    if (recovery != null) {
                        recoveryCode.setText(recovery.getRecoveryCode());
                    }
                }
            }

            @Override
            public void onFailure(Call<Recovery> call, Throwable t) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        finish();
    }
}
