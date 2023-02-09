package com.notmiyouji.newsapp.java.SignInMethod;

import static com.notmiyouji.newsapp.java.Retrofit.NewsAPPAPI.getAPIClient;

import android.app.ActivityOptions;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.Signed.VerifyAccountForm;
import com.notmiyouji.newsapp.kotlin.Gravatar.RequestImage;
import com.notmiyouji.newsapp.kotlin.LoginedModel.SignIn;
import com.notmiyouji.newsapp.kotlin.RetrofitInterface.NewsAPPInterface;
import com.notmiyouji.newsapp.kotlin.SharedSettings.SaveUserLogined;

import retrofit2.Call;

public class EmailMethod {

    private FirebaseAuth mAuth;
    private Button signInButton, signUpButton;
    private Intent intent;
    private AppCompatActivity activity;
    NewsAPPInterface newsAPPInterface = getAPIClient().create(NewsAPPInterface.class);

    public EmailMethod(AppCompatActivity activity, Button signInButton, Button signUpButton) {
        this.activity = activity;
        this.signInButton = signInButton;
        this.signUpButton = signUpButton;
        mAuth = FirebaseAuth.getInstance();
    }
    public void SignInMethod(String account, String password) {
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
                            //Firebase Sign In
                            mAuth = FirebaseAuth.getInstance();
                            mAuth.signInWithEmailAndPassword(signIn.getEmail(), password);
                            //Save user data to Shared Preferences
                            RequestImage requestImage = new RequestImage(signIn.getEmail());
                            String avatar = requestImage.getGravatarURL();
                            SaveUserLogined saveUserLogined = new SaveUserLogined(activity);
                            saveUserLogined.saveUserLogined(signIn.getUserId(), signIn.getName(), signIn.getEmail(), password, signIn.getNickname(), avatar,"login");
                            saveUserLogined.saveBirthday(signIn.getBirthday());
                            saveUserLogined.saveGender(signIn.getGender());
                            //If account verify, go to main page
                            Toast.makeText(activity, R.string.sign_in_success, Toast.LENGTH_SHORT).show();
                            signInButton.setEnabled(true);
                            signUpButton.setEnabled(true);
                            activity.finish();
                            //restart application
                            intent = activity.getBaseContext().getPackageManager().getLaunchIntentForPackage(activity.getBaseContext().getPackageName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            activity.startActivity(intent);
                        } else {
                            Intent intent = new Intent(activity, VerifyAccountForm.class);
                            intent.putExtra("email", signIn.getEmail());
                            intent.putExtra("password", password);
                            intent.putExtra("username", signIn.getNickname());
                            activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                            signInButton.setEnabled(true);
                            signUpButton.setEnabled(true);
                        }

                    } else if (signIn.getStatus().equals("fail")) {
                        Toast.makeText(activity, R.string.Error_login, Toast.LENGTH_SHORT).show();
                        signInButton.setEnabled(true);
                        signUpButton.setEnabled(true);
                    }
                } else {
                    Toast.makeText(activity, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
                    signInButton.setEnabled(true);
                    signUpButton.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<SignIn> call, Throwable t) {
                signInButton.setEnabled(true);
                signUpButton.setEnabled(true);
            }
        });
    }
}
