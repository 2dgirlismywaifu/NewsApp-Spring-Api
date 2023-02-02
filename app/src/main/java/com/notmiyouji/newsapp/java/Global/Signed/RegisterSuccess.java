package com.notmiyouji.newsapp.java.Global.Signed;

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
        //Sign In now
        signinbtn = findViewById(R.id.SignInNow);
        signinbtn.setOnClickListener(v -> {
            SignInMethod(email, password);
        });
        //Show recovery code
        ShowRecoveryCode(email);
    }

    //Sign In now
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
                            SaveUserLogined saveUserLogined = new SaveUserLogined(RegisterSuccess.this);
                            saveUserLogined.saveUserLogined(signIn.getName(), signIn.getEmail(), password, signIn.getNickname(), signIn.getAvatar(),"login");
                            //If account verify, go to main page
                            Toast.makeText(RegisterSuccess.this, R.string.sign_in_success, Toast.LENGTH_SHORT).show();
                            RegisterSuccess.this.finish();
                            //restart application
                            Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SignIn> call, Throwable t) {
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
