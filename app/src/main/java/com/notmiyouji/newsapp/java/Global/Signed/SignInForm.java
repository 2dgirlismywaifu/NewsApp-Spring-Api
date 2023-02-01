package com.notmiyouji.newsapp.java.Global.Signed;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.Retrofit.NewsAPPAPI;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.LoginedModel.SignIn;
import com.notmiyouji.newsapp.kotlin.RetrofitInterface.NewsAPPInterface;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.SharedSettings.SaveUserLogined;

import retrofit2.Call;

public class SignInForm extends AppCompatActivity {

    Button SignInBtn ,SignUpBtn, forgotpassbtn;
    Intent intent;
    TextInputEditText account, password;
    LoadFollowLanguageSystem loadFollowLanguageSystem;
    NewsAPPInterface newsAPPInterface = NewsAPPAPI.getAPIClient().create(NewsAPPInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_form);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();

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
               //Retrofit call signin request
                SignInMethod(account.getText().toString(), password.getText().toString());
            }
        });
    }

    private void SignInMethod(String account, String password) {
        //Retrofit call signin request
        Call<SignIn> call = newsAPPInterface.signIn(account, password);
        call.enqueue(new retrofit2.Callback<SignIn>() {
            @Override
            public void onResponse(Call<SignIn> call, retrofit2.Response<SignIn> response) {
                if (response.isSuccessful()) {
                    SignIn signIn = response.body();
                    if (signIn.getStatus().equals("pass")) {
                        //Check Account Verify or not, if not go to verify page to continue
                        if (signIn.getVerify().equals("true")) {
                            //Save user data to Shared Preferences
                            SaveUserLogined saveUserLogined = new SaveUserLogined(SignInForm.this);
                            saveUserLogined.saveUserLogined(signIn.getEmail(), password, signIn.getNickname(), "login");
                            //If account verify, go to main page
                            Toast.makeText(SignInForm.this, R.string.sign_in_success, Toast.LENGTH_SHORT).show();
                            SignInBtn.setEnabled(true);
                            SignUpBtn.setEnabled(true);
                            SignInForm.this.finish();
                            //restart application
                            Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(SignInForm.this, VerifyAccountForm.class);
                            intent.putExtra("email", signIn.getEmail());
                            intent.putExtra("password", password);
                            intent.putExtra("username", signIn.getNickname());
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SignInForm.this).toBundle());
                            SignInBtn.setEnabled(true);
                            SignUpBtn.setEnabled(true);
                        }

                    } else {
                        SignInBtn.setEnabled(true);
                        SignUpBtn.setEnabled(true);
                    }
                } else {
                    SignInBtn.setEnabled(true);
                    SignUpBtn.setEnabled(true);
                }
            }
            @Override
            public void onFailure(Call<SignIn> call, Throwable t) {
                SignInBtn.setEnabled(true);
                SignUpBtn.setEnabled(true);
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