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

import static com.notmiyouji.newsapp.java.retrofit.NewsAppApi.getAPIClient;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.NewsAppInterface;
import com.notmiyouji.newsapp.kotlin.Utils;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadFollowLanguageSystem;

import java.util.Objects;

import retrofit2.Call;

public class ForgotPasswordForm extends AppCompatActivity {
    private LoadFollowLanguageSystem loadFollowLanguageSystem;
    private TextInputEditText recoveryCodeInput;
    private final NewsAppInterface newsAPPInterface = getAPIClient().create(NewsAppInterface.class);

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
            getOnBackPressedDispatcher().onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //Verify button recovery code
        Button verifyButton = findViewById(R.id.VerifiedButton);
        verifyButton.setOnClickListener(v -> {
            String recoveryCode = String.valueOf(recoveryCodeInput.getText());
            if (recoveryCode.isEmpty()) {
                recoveryCodeInput.setError(getString(R.string.recovery_code_is_required));
                Toast.makeText(this, R.string.recovery_code_is_required, Toast.LENGTH_SHORT).show();
            } else if (recoveryCode.matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                    //Use firebase to send email to reset password
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.sendPasswordResetEmail(recoveryCode).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, R.string.email_reset_password, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                verifyRecoveryCode(recoveryCode);
            }


        });
    }

    private void verifyRecoveryCode(String recoveryCode) {
        Call<com.notmiyouji.newsapp.kotlin.model.RecoveryAccount> signInCall = newsAPPInterface.recoveryAccountByRecoveryCode(Utils.encodeToBase64(recoveryCode));
        assert signInCall != null;
        signInCall.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<com.notmiyouji.newsapp.kotlin.model.RecoveryAccount> call,
                                   @NonNull retrofit2.Response<com.notmiyouji.newsapp.kotlin.model.RecoveryAccount> response) {
                if (response.isSuccessful()) {
                    com.notmiyouji.newsapp.kotlin.model.RecoveryAccount recoveryAccount = response.body();
                    assert recoveryAccount != null;
                    if (Objects.equals(recoveryAccount.getStatus(), "success")) {
                        Intent intent = new Intent(ForgotPasswordForm.this, RecoveryAccount.class);
                        intent.putExtra("userid", recoveryAccount.getUserId());
                        intent.putExtra("email", recoveryAccount.getEmail());
                        intent.putExtra("fullname", recoveryAccount.getFullName());
                        intent.putExtra("username", recoveryAccount.getNickName());
                        intent.putExtra("code", recoveryCode);
                        startActivity(intent);
                        ActivityOptions.makeSceneTransitionAnimation(ForgotPasswordForm.this).toBundle();
                        finish();
                    } else {
                        Toast.makeText(ForgotPasswordForm.this, R.string.recovery_code_not_matched, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<com.notmiyouji.newsapp.kotlin.model.RecoveryAccount> call, @NonNull Throwable t) {
            }
        });
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            ActivityOptions.makeSceneTransitionAnimation(ForgotPasswordForm.this).toBundle();
            finish();
        }
    };

    public OnBackPressedCallback getCallback() {
        return callback;
    }

    public void onResume() {
        super.onResume();
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
    }
}