package com.notmiyouji.newsapp.java.Global.Signed;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.Retrofit.NewsAPPAPI;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.LoginedModel.Recovery;
import com.notmiyouji.newsapp.kotlin.RetrofitInterface.NewsAPPInterface;
import com.notmiyouji.newsapp.kotlin.SharedSettings.GetUserLogined;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadFollowLanguageSystem;

import retrofit2.Call;

public class RegisterSuccess extends AppCompatActivity {
    LoadFollowLanguageSystem loadFollowLanguageSystem;
    NewsAPPInterface newsAPPInterface = NewsAPPAPI.getAPIClient().create(NewsAPPInterface.class);
    protected void onCreate(android.os.Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_success);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        //Get email logined from shared preferences
        GetUserLogined getUserLogined = new GetUserLogined(this);
        String email = getUserLogined.getEmail();
        //Show recovery code
        ShowRecoveryCode(email);
    }

    private void ShowRecoveryCode (String email) {
        Call<Recovery> call = newsAPPInterface.recoveryCode(new Recovery(email));
        call.enqueue(new retrofit2.Callback<Recovery>() {
            @Override
            public void onResponse(Call<Recovery> call, retrofit2.Response<Recovery> response) {
                if (response.isSuccessful()) {
                    Recovery recovery = response.body();
                    if (recovery != null) {
                        TextInputEditText recoveryCode = findViewById(R.id.recovey_code);
                        recoveryCode.setText(recovery.getRecoverycode());
                    }
                }
            }

            @Override
            public void onFailure(Call<Recovery> call, Throwable t) {

            }
        });
    }
}
