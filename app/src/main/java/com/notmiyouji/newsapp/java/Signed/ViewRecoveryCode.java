package com.notmiyouji.newsapp.java.Signed;

import android.app.ActivityOptions;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.Retrofit.NewsAPPAPI;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;
import com.notmiyouji.newsapp.kotlin.LoginedModel.Recovery;
import com.notmiyouji.newsapp.kotlin.RetrofitInterface.NewsAPPInterface;
import com.notmiyouji.newsapp.kotlin.SharedSettings.GetUserLogined;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadThemeShared;
import com.notmiyouji.newsapp.kotlin.UpdateModel.RecoveryCode;

import retrofit2.Call;

public class ViewRecoveryCode extends AppCompatActivity {
    LoadFollowLanguageSystem loadFollowLanguageSystem;
    LoadThemeShared loadThemeShared;
    GetUserLogined getUserLogined;
    ShapeableImageView avatar;
    TextView fullName, username, recoveryCodeText;
    Button recoveryCodeButton;
    NewsAPPInterface newsAPPInterface = NewsAPPAPI.getAPIClient().create(NewsAPPInterface.class);

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

        getUserLogined = new GetUserLogined(this);
        //Set textView
        fullName = findViewById(R.id.fullname);
        username = findViewById(R.id.username);
        recoveryCodeText = findViewById(R.id.return_recovery_code);
        recoveryCodeButton = findViewById(R.id.request_new_recovery_code);
        fullName.setText(getUserLogined.getFullname());
        username.setText("@" + getUserLogined.getUsername());
        //Copy recovery code to clipboard
        recoveryCodeText.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("recovery", recoveryCodeText.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
        });
        //Load avatar
        avatar = findViewById(R.id.avatar_user_logined);
        LoadImageURL loadImageURL = new LoadImageURL(getUserLogined.getAvatar());
        loadImageURL.loadImageUser(avatar);
        //back button
        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //Show recovery code
        ShowRecoveryCode(getUserLogined.getEmail());
        //Request new recovery code
        recoveryCodeButton.setOnClickListener(v -> {
            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
            materialAlertDialogBuilder.setIcon(R.mipmap.ic_launcher);
            materialAlertDialogBuilder.setTitle("Request new recovery code");
            materialAlertDialogBuilder.setMessage("Are you sure you want to request a new recovery code?");
            materialAlertDialogBuilder.setPositiveButton(R.string.yes, (dialog, which) -> {
                updateRecoveryCode(getUserLogined.getUserID());
            });
            materialAlertDialogBuilder.setNegativeButton(R.string.no, (dialog, which) -> {
                dialog.dismiss();
            });
            materialAlertDialogBuilder.show();
        });
    }

    private void updateRecoveryCode(String userid) {
        //Retrofit call update recovery code request
        Call<RecoveryCode> call = newsAPPInterface.generateRecoveryCode(userid);
        call.enqueue(new retrofit2.Callback<RecoveryCode>() {
            @Override
            public void onResponse(Call<RecoveryCode> call, retrofit2.Response<RecoveryCode> response) {
                if (response.isSuccessful()) {
                    RecoveryCode recoveryCode = response.body();
                    if (recoveryCode.getStatus().equals("pass")) {
                        recoveryCodeText.setText(recoveryCode.getCode());
                        Toast.makeText(ViewRecoveryCode.this, R.string.recovery_code_updated, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ViewRecoveryCode.this, R.string.recovery_code_updated_failed, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RecoveryCode> call, Throwable t) {

            }
        });
    }

    private void ShowRecoveryCode(String email) {
        Call<Recovery> call = newsAPPInterface.recoveryCode(email);
        call.enqueue(new retrofit2.Callback<Recovery>() {
            @Override
            public void onResponse(Call<Recovery> call, retrofit2.Response<Recovery> response) {
                if (response.isSuccessful()) {
                    Recovery recovery = response.body();
                    if (recovery != null) {
                        recoveryCodeText.setText(recovery.getRecoveryCode());
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
    @Override
    public void onResume() {
        super.onResume();
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
    }
}