package com.notmiyouji.newsapp.java.Signed;

import android.app.ActivityOptions;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.UpdateInformation.PasswordController;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;
import com.notmiyouji.newsapp.kotlin.SharedSettings.GetUserLogined;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadFollowLanguageSystem;

public class ChangePassword extends AppCompatActivity {
    LoadFollowLanguageSystem loadFollowLanguageSystem;
    TextInputEditText oldPassword, newPassword, confirmPassword;
    TextView fullName, username;
    GetUserLogined getUserLogined;
    Button changePassword;
    ShapeableImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();

        getUserLogined = new GetUserLogined(this);
        fullName = findViewById(R.id.fullname);
        username = findViewById(R.id.username);
        fullName.setText(getUserLogined.getFullname());
        username.setText("@" + getUserLogined.getUsername());
        //Password Input
        oldPassword = findViewById(R.id.oldpassword_input);
        newPassword = findViewById(R.id.newspass_user_input);
        confirmPassword = findViewById(R.id.confirmpass_input);
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
        //Change Password Button
        changePassword = findViewById(R.id.ChangeButton);
        changePassword.setOnClickListener(v -> {
            String oldPasswordString = oldPassword.getText().toString();
            String newPasswordString = newPassword.getText().toString();
            String confirmPasswordString = confirmPassword.getText().toString();
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
                materialAlertDialogBuilder.setNegativeButton(R.string.no, (dialog, which) -> {
                    dialog.dismiss();
                });
                materialAlertDialogBuilder.setPositiveButton(R.string.yes, (dialog, which) -> {
                    //Update Password Controller
                    PasswordController passwordController = new PasswordController(
                            getUserLogined.getUserID(),
                            getUserLogined.getEmail(),
                            oldPasswordString, newPasswordString, confirmPasswordString,
                            ChangePassword.this);
                    passwordController.CheckPassword();
                });
                materialAlertDialogBuilder.show();

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
    }
}