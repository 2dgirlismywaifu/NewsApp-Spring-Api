package com.notmiyouji.newsapp.java.Global.Signed;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.Retrofit.NewsAPPAPI;
import com.notmiyouji.newsapp.java.SharedSettings.LanguagePrefManager;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.LoginedModel.CheckNickName;
import com.notmiyouji.newsapp.kotlin.LoginedModel.Register;
import com.notmiyouji.newsapp.kotlin.RetrofitInterface.NewsAPPInterface;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadFollowLanguageSystem;

import retrofit2.Call;
import retrofit2.Response;

public class SignUpForm extends AppCompatActivity {

    Button signupbtn, signinbtn;
    TextInputEditText email, password, confirmpassword, username;
    LoadFollowLanguageSystem loadFollowLanguageSystem;
    NewsAPPInterface newsAPPInterface = NewsAPPAPI.getAPIClient().create(NewsAPPInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_form);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();

        email = findViewById(R.id.email_input);
        password = findViewById(R.id.password_input);
        confirmpassword = findViewById(R.id.Repassword_input);
        username = findViewById(R.id.nickname_input);
        signinbtn = findViewById(R.id.ResendCodeBtn);
        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //Sign Up form only access from Sign In form
        signinbtn.setOnClickListener(v -> {
            onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //sign up button
        signupbtn = findViewById(R.id.SignUpNow);
        signupbtn.setOnClickListener(v -> {
            //check input
            checkInput();
            //check nickname
            checkNickname();
            //if all input is not null, sign up
            if (!email.getText().toString().isEmpty()
                    && !password.getText().toString().isEmpty()
                    && !confirmpassword.getText().toString().isEmpty()
                    && !username.getText().toString().isEmpty()) {
                //if password and confirm password is not same, show error
                if (!password.getText().toString().equals(confirmpassword.getText().toString())) {
                    password.setError("Password is not same");
                    confirmpassword.setError("Password is not same");
                } else {
                    //if password and confirm password is same, sign up
                    //sign up function
                    RegisterAccount(email.getText().toString(), password.getText().toString(), username.getText().toString());
                }
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

    private void checkInput() {
        //if all input is null, textinputlayout will show error
        if (email.getText().toString().isEmpty()) {
            email.setError("Email is required");
        }
        if (password.getText().toString().isEmpty()) {
            password.setError("Password is required");
        }
        if (confirmpassword.getText().toString().isEmpty()) {
            confirmpassword.setError("Confirm password is required");
        }
        if (username.getText().toString().isEmpty()) {
            username.setError("Username is required");
        }
    }
    private void checkNickname() {
        //Use Retrofit to check nickname
        Call<CheckNickName> call = newsAPPInterface.checkNickname(
                new CheckNickName(username.getText().toString(), email.getText().toString()));
        call.enqueue(new retrofit2.Callback<CheckNickName>() {

            @Override
            public void onResponse(Call<CheckNickName> call, Response<CheckNickName> response) {
                if (response.body().getNickname().equals(username.getText().toString())) {
                    username.setError("Nickname is already used");
                }
                if (response.body().getEmail().equals(email.getText().toString())) {
                    email.setError("Email is already used");
                }
            }
            @Override
            public void onFailure(Call<CheckNickName> call, Throwable t) {

            }
        });
    }

    private void RegisterAccount(String email, String password, String username) {
        Call<Register> call = newsAPPInterface.register(
                new Register(email, password, username));
        call.enqueue(new retrofit2.Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                //Save user account to firebase realtime database
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password);
                //go to verify account activity
                Intent intent = new Intent(SignUpForm.this, VerifyAccountForm.class);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                intent.putExtra("username", username);
                //send verify code to email with firebase authentication
                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                ActivityOptions.makeSceneTransitionAnimation(SignUpForm.this).toBundle();
                SignUpForm.this.finish();
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<Register> call, Throwable t) {

            }
        });
    }
}