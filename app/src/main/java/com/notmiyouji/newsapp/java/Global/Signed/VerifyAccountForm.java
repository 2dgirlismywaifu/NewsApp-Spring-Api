package com.notmiyouji.newsapp.java.Global.Signed;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.Retrofit.NewsAPPAPI;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.LoginedModel.Verify;
import com.notmiyouji.newsapp.kotlin.RetrofitInterface.NewsAPPInterface;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.SharedSettings.SaveUserLogined;

import java.util.Timer;

import retrofit2.Call;

public class VerifyAccountForm extends AppCompatActivity {
    LoadFollowLanguageSystem loadFollowLanguageSystem;
    Button verifybtn, resendbtn;
    NewsAPPInterface newsAPPInterface = NewsAPPAPI.getAPIClient().create(NewsAPPInterface.class);
    String userID, fullname, email, password, username;

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
        fullname = getIntent().getStringExtra("fullname");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        username = getIntent().getStringExtra("username");
        TextView emailtext = findViewById(R.id.email_send);
        emailtext.setText(email);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //check if user is verified or not
        verifybtn = findViewById(R.id.VerifiedButton);
        verifybtn.setOnClickListener(v -> {
            //re-sign in user
            FirebaseAuth authVerifed = FirebaseAuth.getInstance();
            authVerifed.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser userVerifed = authVerifed.getCurrentUser();
                    if (userVerifed.isEmailVerified()) {
                        //if user is verified, change verified status to true
                        updateStatus(email);
                        //After that, save user account to shared preferences
                        SaveUserLogined saveUserLogined = new SaveUserLogined(this);
                        saveUserLogined.saveUserLogined(userID, fullname, email, password, username, "not_available","true");
                        //go to RegisteoSuccesful form
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
                onBackPressed();
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
                finish();
            });
            builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
            builder.show();
        });
        //resend verification email
        resendbtn = findViewById(R.id.ResendCodeBtn);
        resendbtn.setOnClickListener(v -> {
            resendbtn.setEnabled(false);
            //send verification email and wait 60 second to send again
            user.sendEmailVerification().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, R.string.verification_email_sent_to_your_mailbox, Toast.LENGTH_SHORT).show();
                    Timer timer = new Timer();
                    timer.schedule(new java.util.TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(() -> resendbtn.setEnabled(true));
                        }
                    }, 300);
                } else {
                    Toast.makeText(this, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
                    resendbtn.setEnabled(true);
                }
            });

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(R.string.verify_account);
        builder.setMessage(R.string.your_account_is_not_verified_yet_are_you_sure_to_go_back);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
    }
    private void updateStatus(String email) {
        Call<Verify> call = newsAPPInterface.verify(email);
        call.enqueue(new retrofit2.Callback<Verify>() {
            @Override
            public void onResponse(@NonNull Call<Verify> call, retrofit2.Response<Verify> response) {
                if (response.isSuccessful()) {
                    if (response.body().getVerifyStatus().equals("true")) {
                        Toast.makeText(VerifyAccountForm.this, "Account Verified Success", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Verify> call, Throwable t) {
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