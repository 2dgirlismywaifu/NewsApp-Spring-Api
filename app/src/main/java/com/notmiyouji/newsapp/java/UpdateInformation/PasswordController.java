package com.notmiyouji.newsapp.java.UpdateInformation;

import static com.notmiyouji.newsapp.java.Retrofit.NewsAPPAPI.getAPIClient;

import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.RetrofitInterface.NewsAPPInterface;
import com.notmiyouji.newsapp.kotlin.SharedSettings.SaveUserLogined;
import com.notmiyouji.newsapp.kotlin.UpdateModel.CheckPassword;
import com.notmiyouji.newsapp.kotlin.UpdateModel.Password;

import retrofit2.Call;

public class PasswordController {
    String userid;
    String email;
    String oldpassword;
    String newpassword;
    String confirmpassword;
    AppCompatActivity activity;
    NewsAPPInterface newsAPPInterface = getAPIClient().create(NewsAPPInterface.class);
    public PasswordController(String userid, String email, String oldpassword, String newpassword, String confirmpassword, AppCompatActivity activity) {
        this.userid = userid;
        this.email = email;
        this.oldpassword = oldpassword;
        this.newpassword = newpassword;
        this.confirmpassword = confirmpassword;
        this.activity = activity;
    }

    public void CheckPassword() {
        //Retrofit call check old password request
        Call<CheckPassword> checkPassNow = newsAPPInterface.checkPasswordAccount(userid, email, oldpassword);
        checkPassNow.enqueue(new retrofit2.Callback<CheckPassword>() {
            @Override
            public void onResponse(Call<CheckPassword> call, retrofit2.Response<CheckPassword> response) {
                if (response.isSuccessful()) {
                    CheckPassword checkPassword = response.body();
                    if (checkPassword.getStatus().equals("pass")) {
                        UpdatePassword();
                    } else {
                        Toast.makeText(activity, R.string.oldpass_error, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckPassword> call, Throwable t) {

            }
        });
    }

    private void UpdatePassword() {
        //Retrofit call update password request
        Call<Password> updatePasswordNow = newsAPPInterface.updatePasswordAccount(userid,newpassword);
        updatePasswordNow.enqueue(new retrofit2.Callback<Password>() {
            @Override
            public void onResponse(Call<Password> call, retrofit2.Response<Password> response) {
                if (response.isSuccessful()) {
                    Password password = response.body();
                    if (password.getStatus().equals("pass")) {
                        Toast.makeText(activity, R.string.password_updated, Toast.LENGTH_SHORT).show();
                        //Update password in firebase
                        FirebaseAuth.getInstance().getCurrentUser().updatePassword(newpassword);
                        //Sign out
                        FirebaseAuth.getInstance().signOut();
                        SaveUserLogined saveUserLogined = new SaveUserLogined(activity);
                        saveUserLogined.saveUserLogined("", "", "", "", "", "","");
                        saveUserLogined.saveBirthday("");
                        saveUserLogined.saveGender("");
                        //Restart application
                        Toast.makeText(activity, R.string.sign_out_success, Toast.LENGTH_SHORT).show();
                        Intent intent = activity.getBaseContext().getPackageManager().getLaunchIntentForPackage(
                                activity.getBaseContext().getPackageName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent);
                    } else {
                        Toast.makeText(activity, R.string.password_updated_failed, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Password> call, Throwable t) {

            }
        });
    }

}
