package com.notmiyouji.newsapp.java.Global.Signed;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.Retrofit.NewsAPPAPI;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.LoginedModel.Verify;
import com.notmiyouji.newsapp.kotlin.RetrofitInterface.NewsAPPInterface;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.SharedSettings.SaveUserLogined;

import retrofit2.Call;

public class VerifyAccountForm extends AppCompatActivity {
    LoadFollowLanguageSystem loadFollowLanguageSystem;
    Button verifybtn, resendbtn;
    NewsAPPInterface newsAPPInterface = NewsAPPAPI.getAPIClient().create(NewsAPPInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_account_form);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        //Get string send from Sign Up form
        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");
        String username = getIntent().getStringExtra("username");
        //check if user is verified or not
        verifybtn = findViewById(R.id.VerifiedButton);
        verifybtn.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user.isEmailVerified()) {
                //if user is verified, change verified status to true
                updateStatus(email);
                //After that, save user account to shared preferences
                SaveUserLogined saveUserLogined = new SaveUserLogined(this);
                saveUserLogined.saveUserLogined(email, password, username);
                //go to RegisteoSuccesful form
                Intent intent = new Intent(this, RegisterSuccess.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Please verify your email first with link sent to your mailbox", Toast.LENGTH_SHORT).show();
            }
        });
        //resend verification email
        resendbtn = findViewById(R.id.ResendCodeBtn);
        resendbtn.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            user.sendEmailVerification().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Verification email sent to your mailbox", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void updateStatus(String email) {
        Call<Verify> call = newsAPPInterface.verifiy(new Verify(email));
        call.enqueue(new retrofit2.Callback<Verify>() {
            @Override
            public void onResponse(Call<Verify> call, retrofit2.Response<Verify> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(VerifyAccountForm.this, "Account Verified Success", Toast.LENGTH_SHORT).show();
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