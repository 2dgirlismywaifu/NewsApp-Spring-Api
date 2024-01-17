/*
 * Copyright By @2dgirlismywaifu (2023) .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.notmiyouji.newsapp.java.loginmethod;

import static com.notmiyouji.newsapp.java.retrofit.NewsAppApi.getAPIClient;

import android.app.ActivityOptions;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.activity.userlogin.VerifyAccountForm;
import com.notmiyouji.newsapp.kotlin.NewsAppInterface;
import com.notmiyouji.newsapp.kotlin.Utils;
import com.notmiyouji.newsapp.kotlin.gravatar.RequestImage;
import com.notmiyouji.newsapp.kotlin.model.SignIn;
import com.notmiyouji.newsapp.kotlin.sharedsettings.SaveUserLogined;

import java.util.Objects;

import retrofit2.Call;

public class EmailMethod {

    NewsAppInterface newsAPPInterface = getAPIClient().create(NewsAppInterface.class);
    private FirebaseAuth mAuth;
    private final Button signInButton;
    private final Button signUpButton;
    private Intent intent;
    private final AppCompatActivity activity;

    public EmailMethod(AppCompatActivity activity, Button signInButton, Button signUpButton) {
        this.activity = activity;
        this.signInButton = signInButton;
        this.signUpButton = signUpButton;
        mAuth = FirebaseAuth.getInstance();
    }

    private String encodeData(String data) {
        return Utils.encodeToBase64(data);
    }

    public void SignInMethod(String account, String password) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(account, password).addOnCompleteListener(activity, task -> {
            if (task.isSuccessful()) {
                //Retrofit call sign in request
                String userToken = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                verifyWithDatabase(account, password, userToken);
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(activity, R.string.Error_login, Toast.LENGTH_SHORT).show();
                signInButton.setEnabled(true);
                signUpButton.setEnabled(true);
            }
        });


    }

    private void verifyWithDatabase(String account, String password, String userToken) {
        Call<SignIn> call = newsAPPInterface.signIn(Utils.encodeToBase64(account), Utils.encodeToBase64(userToken));
        assert call != null;
        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<SignIn> call, @NonNull retrofit2.Response<SignIn> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        SignIn signIn = response.body();
                        if (Objects.equals(signIn.getStatus(), "success")) {
                            //Check Account Verify or not, if not go to verify page to continue
                            if (Objects.equals(signIn.getVerify(), "true")) {
                                //Save user data to Shared Preferences
                                String avatar;
                                if (signIn.getAvatar() != null) {
                                    if (signIn.getAvatar().isEmpty()) {
                                        RequestImage requestImage = new RequestImage(Objects.requireNonNull(signIn.getEmail()));
                                        avatar = requestImage.getGravatarURL();
                                    } else {
                                        avatar = signIn.getAvatar();
                                    }
                                } else {
                                    RequestImage requestImage = new RequestImage(Objects.requireNonNull(signIn.getEmail()));
                                    avatar = requestImage.getGravatarURL();
                                }
                                SaveUserLogined saveUserLogined = new SaveUserLogined(activity);
                                saveUserLogined.saveUserLogin(signIn.getUserId(), signIn.getFullName(), signIn.getEmail(), encodeData(password), signIn.getNickName(), avatar, "login");
                                saveUserLogined.saveBirthday(signIn.getBirthday());
                                saveUserLogined.saveGender(signIn.getGender());
                                //If account verify, go to main page
                                Toast.makeText(activity, R.string.sign_in_success, Toast.LENGTH_SHORT).show();
                                signInButton.setEnabled(true);
                                signUpButton.setEnabled(true);
                                activity.finish();
                                //restart application
                                intent = activity.getBaseContext().getPackageManager().getLaunchIntentForPackage(activity.getBaseContext().getPackageName());
                                assert intent != null;
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                activity.startActivity(intent);
                            } else {
                                Intent verifyForm = new Intent(activity, VerifyAccountForm.class);
                                verifyForm.putExtra("email", signIn.getEmail());
                                verifyForm.putExtra("password", password);
                                verifyForm.putExtra("username", signIn.getNickName());
                                activity.startActivity(verifyForm, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                                signInButton.setEnabled(true);
                                signUpButton.setEnabled(true);
                            }
                        } else if (Objects.equals(signIn.getStatus(), "fail")) {
                            Toast.makeText(activity, R.string.Error_login, Toast.LENGTH_SHORT).show();
                            signInButton.setEnabled(true);
                            signUpButton.setEnabled(true);
                        }
                    }
                } else {
                    Toast.makeText(activity, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
                    signInButton.setEnabled(true);
                    signUpButton.setEnabled(true);
                }
            }
            @Override
            public void onFailure(@NonNull Call<SignIn> call, @NonNull Throwable t) {
                Toast.makeText(activity, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
                signInButton.setEnabled(true);
                signUpButton.setEnabled(true);
            }
        });
    }
}
