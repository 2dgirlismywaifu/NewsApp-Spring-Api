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

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;
import com.notmiyouji.newsapp.kotlin.sharedsettings.GetUserLogin;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadThemeShared;

public class ChangePassword extends AppCompatActivity {
    LoadFollowLanguageSystem loadFollowLanguageSystem;
    LoadThemeShared loadThemeShared;
    TextInputEditText oldPassword, newPassword, confirmPassword;
    TextView fullName, username;
    GetUserLogin getUserLogin;
    Button changePassword;
    ShapeableImageView avatar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();

        getUserLogin = new GetUserLogin(this);
        fullName = findViewById(R.id.fullname);
        username = findViewById(R.id.username);
        fullName.setText(getUserLogin.getFullName());
        username.setText("@" + getUserLogin.getUsername());
        //Password Input
        oldPassword = findViewById(R.id.oldpassword_input);
        newPassword = findViewById(R.id.newspass_user_input);
        confirmPassword = findViewById(R.id.confirmpass_input);
        //Load avatar
        avatar = findViewById(R.id.avatar_user_logined);
        LoadImageURL loadImageURL = new LoadImageURL(getUserLogin.getAvatar());
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
            String oldPasswordString = String.valueOf(oldPassword.getText());
            String newPasswordString = String.valueOf(newPassword.getText());
            String confirmPasswordString = String.valueOf(confirmPassword.getText());
            if (oldPasswordString.isEmpty() || newPasswordString.isEmpty() || confirmPasswordString.isEmpty()) {
                Toast.makeText(ChangePassword.this, R.string.password_empty, Toast.LENGTH_SHORT).show();
            } else if (!newPasswordString.equals(confirmPasswordString)) {
                Toast.makeText(ChangePassword.this, R.string.password_is_not_same, Toast.LENGTH_SHORT).show();
            } else {
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(ChangePassword.this);
                materialAlertDialogBuilder.setIcon(R.mipmap.ic_launcher);
                materialAlertDialogBuilder.setTitle(R.string.change_password_account);
                materialAlertDialogBuilder.setMessage("Change password account will logout your account. Are you sure?");
                materialAlertDialogBuilder.setPositiveButton(R.string.yes, (dialog, which) -> {
                    Toast.makeText(ChangePassword.this, R.string.change_password_account, Toast.LENGTH_SHORT).show();
                    finish();
                });
                materialAlertDialogBuilder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
                materialAlertDialogBuilder.setPositiveButton(R.string.yes, (dialog, which) -> {
                    //Update Password Controller
                    UpdateInformation updateInformation = new UpdateInformation(getUserLogin.getUserID(), this);
                    updateInformation.updatePassword(getUserLogin.getEmail(), oldPasswordString, newPasswordString, "");
                });
                materialAlertDialogBuilder.show();

            }
        });
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            ActivityOptions.makeSceneTransitionAnimation(ChangePassword.this).toBundle();
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
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
    }
}