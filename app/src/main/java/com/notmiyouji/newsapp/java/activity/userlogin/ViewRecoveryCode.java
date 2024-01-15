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
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.retrofit.NewsAppApi;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;
import com.notmiyouji.newsapp.kotlin.NewsAppInterface;
import com.notmiyouji.newsapp.kotlin.Utils;
import com.notmiyouji.newsapp.kotlin.model.RecoveryCode;
import com.notmiyouji.newsapp.kotlin.sharedsettings.GetUserLogin;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadThemeShared;

import retrofit2.Call;

public class ViewRecoveryCode extends AppCompatActivity {
    private final NewsAppInterface newsAPPInterface = NewsAppApi.getAPIClient().create(NewsAppInterface.class);
    private LoadFollowLanguageSystem loadFollowLanguageSystem;
    private LoadThemeShared loadThemeShared;
    private GetUserLogin getUserLogin;
    private TextView recoveryCodeText;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recovery_code);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();

        getUserLogin = new GetUserLogin(this);
        //Set textView
        TextView fullName = findViewById(R.id.fullname);
        TextView username = findViewById(R.id.username);
        recoveryCodeText = findViewById(R.id.return_recovery_code);
        Button recoveryCodeButton = findViewById(R.id.request_new_recovery_code);
        fullName.setText(getUserLogin.getFullName());
        username.setText("@" + getUserLogin.getUsername());
        //Copy recovery code to clipboard
        recoveryCodeText.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("recovery", recoveryCodeText.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
        });
        //Load avatar
        ShapeableImageView avatar = findViewById(R.id.avatar_user_logined);
        LoadImageURL loadImageURL = new LoadImageURL(getUserLogin.getAvatar());
        loadImageURL.loadImageUser(avatar);
        //back button
        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //Show recovery code
        ShowRecoveryCode(getUserLogin.getEmail());
        //Request new recovery code
        recoveryCodeButton.setOnClickListener(v -> {
            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
            materialAlertDialogBuilder.setIcon(R.mipmap.ic_launcher);
            materialAlertDialogBuilder.setTitle(R.string.request_new_recovery_code);
            materialAlertDialogBuilder.setMessage(R.string.are_you_sure_you_want_to_request_a_new_recovery_code);
            materialAlertDialogBuilder.setPositiveButton(R.string.yes, (dialog, which) -> updateRecoveryCode(getUserLogin.getUserID()));
            materialAlertDialogBuilder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
            materialAlertDialogBuilder.show();
        });
    }

    private void updateRecoveryCode(String userid) {
        //Retrofit call update recovery code request
        Call<RecoveryCode> call = newsAPPInterface.createNewRecoveryCode(userid);
        assert call != null;
        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RecoveryCode> call, @NonNull retrofit2.Response<RecoveryCode> response) {
                if (response.isSuccessful()) {
                    RecoveryCode recoveryCode = response.body();
                    assert recoveryCode != null;
                    recoveryCodeText.setText(recoveryCode.getRecovery());
                    Toast.makeText(ViewRecoveryCode.this, R.string.recovery_code_updated, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecoveryCode> call, @NonNull Throwable t) {
                Toast.makeText(ViewRecoveryCode.this, R.string.recovery_code_updated_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ShowRecoveryCode(String email) {
        Call<RecoveryCode> call = newsAPPInterface.getRecoveryCode(Utils.encodeToBase64(email));
        assert call != null;
        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RecoveryCode> call, @NonNull retrofit2.Response<RecoveryCode> response) {
                if (response.isSuccessful()) {
                    RecoveryCode recoveryCode = response.body();
                    if (recoveryCode != null) {
                        recoveryCodeText.setText(recoveryCode.getRecovery());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecoveryCode> call, @NonNull Throwable t) {
            }
        });
    }
    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            ActivityOptions.makeSceneTransitionAnimation(ViewRecoveryCode.this).toBundle();
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