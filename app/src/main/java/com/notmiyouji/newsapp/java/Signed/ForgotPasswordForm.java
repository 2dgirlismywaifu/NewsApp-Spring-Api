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
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.LoginedModel.SignIn;
import com.notmiyouji.newsapp.kotlin.RetrofitInterface.NewsAPPInterface;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadFollowLanguageSystem;

import retrofit2.Call;

public class ForgotPasswordForm extends AppCompatActivity {
    LoadFollowLanguageSystem loadFollowLanguageSystem;
    TextInputEditText recoveryCodeInput;
    Button verifyButton;
    NewsAPPInterface newsAPPInterface = getAPIClient().create(NewsAPPInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_form);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        recoveryCodeInput = findViewById(R.id.recovey_code);
        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //Verify button recovery code
        verifyButton = findViewById(R.id.VerifiedButton);
        verifyButton.setOnClickListener(v -> {
            String recoveryCode = recoveryCodeInput.getText().toString();
            if (recoveryCode.isEmpty()) {
                recoveryCodeInput.setError(getString(R.string.recovery_code_is_required));
                Toast.makeText(this, R.string.recovery_code_is_required, Toast.LENGTH_SHORT).show();
            }
            else {
                verifyRecoveryCode(recoveryCode);
            }
        });
    }

    private void verifyRecoveryCode(String recoveryCode) {
        Call<SignIn> signInCall = newsAPPInterface.requestRecovery(recoveryCode);
        signInCall.enqueue(new retrofit2.Callback<SignIn>() {
            @Override
            public void onResponse(Call<SignIn> call, retrofit2.Response<SignIn> response) {
                if (response.isSuccessful()) {
                    SignIn signIn = response.body();
                    if (signIn.getStatus().equals("pass")) {
                        Intent intent = new Intent(ForgotPasswordForm.this, RecoveryAccount.class);
                        intent.putExtra("userid", signIn.getUserId());
                        intent.putExtra("email", signIn.getEmail());
                        intent.putExtra("username", signIn.getNickname());
                        intent.putExtra("fullname", signIn.getName());
                        startActivity(intent);
                        ActivityOptions.makeSceneTransitionAnimation(ForgotPasswordForm.this).toBundle();
                        finish();
                    }
                    else {
                        Toast.makeText(ForgotPasswordForm.this, R.string.recovery_code_not_matched, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SignIn> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        finish();
    }

    public void onResume() {
        super.onResume();
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
    }
}