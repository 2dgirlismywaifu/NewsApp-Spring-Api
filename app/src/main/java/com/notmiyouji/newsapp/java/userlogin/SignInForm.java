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

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.loginmethod.EmailMethod;
import com.notmiyouji.newsapp.java.loginmethod.GoogleMethod;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.sharedsettings.SaveUserLogined;

public class SignInForm extends AppCompatActivity {

    Button SignInBtn, SignUpBtn, forgotpassbtn;
    LinearLayout GoogleSSO;
    Intent intent;
    TextInputEditText account, password;
    LoadFollowLanguageSystem loadFollowLanguageSystem;
    SaveUserLogined saveUserLogined;
    GoogleMethod googleMethod;
    ActivityResultLauncher<Intent> googleSSO = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                googleMethod.firebaseAuthWithGoogle(account.getIdToken(), account.getEmail(),
                        account.getDisplayName(), account.getPhotoUrl());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Google Sign In failed", Toast.LENGTH_SHORT).show();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_form);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();

        saveUserLogined = new SaveUserLogined(SignInForm.this);
        account = findViewById(R.id.username_code);
        password = findViewById(R.id.password_input);
        SignUpBtn = findViewById(R.id.SignUpBtn);
        SignUpBtn.setOnClickListener(v -> {
            intent = new Intent(this, SignUpForm.class);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });
        //Forgot Password form only access from Sign In form
        forgotpassbtn = findViewById(R.id.ForgotPasswordBtn);
        forgotpassbtn.setOnClickListener(v -> {
            intent = new Intent(this, ForgotPasswordForm.class);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //Sign In button
        SignInBtn = findViewById(R.id.SignInBtn);
        SignInBtn.setOnClickListener(v -> {
            SignInBtn.setEnabled(false);
            SignUpBtn.setEnabled(false);
            if (account.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                account.setError("Please enter your account");
                password.setError("Please enter your password");
                SignInBtn.setEnabled(true);
                SignUpBtn.setEnabled(true);
            } else {
                EmailMethod emailMethod = new EmailMethod(this, SignInBtn, SignUpBtn);
                emailMethod.SignInMethod(account.getText().toString(), password.getText().toString());
            }
        });
        //Google SSO
        GoogleSSO = findViewById(R.id.Google_signin);
        //Sign in with Google use Firebase
        GoogleSSO.setOnClickListener(v -> {
            SignInBtn.setEnabled(false);
            SignUpBtn.setEnabled(false);
            // Start activity
            //Google SSO
            googleMethod = new GoogleMethod(SignInForm.this, SignInBtn, SignUpBtn);
            Intent signInIntent = googleMethod.getGoogleSignInClient().getSignInIntent();
            googleSSO.launch(signInIntent);
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