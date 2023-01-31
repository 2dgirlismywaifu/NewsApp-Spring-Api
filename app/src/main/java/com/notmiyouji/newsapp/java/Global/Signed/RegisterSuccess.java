package com.notmiyouji.newsapp.java.Global.Signed;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
    TextView recoveryCode;
    NewsAPPInterface newsAPPInterface = NewsAPPAPI.getAPIClient().create(NewsAPPInterface.class);
    protected void onCreate(android.os.Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_success);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        recoveryCode = findViewById(R.id.recovery_code_label);
        //Get email logined from shared preferences
        GetUserLogined getUserLogined = new GetUserLogined(this);
        String email = getUserLogined.getEmail();
        //Show recovery code
        ShowRecoveryCode(email);
    }

    private void ShowRecoveryCode (String email) {
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
}
