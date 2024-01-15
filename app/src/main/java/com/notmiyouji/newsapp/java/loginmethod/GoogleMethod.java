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

import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.activity.userlogin.FixBlurryGoogleImage;
import com.notmiyouji.newsapp.java.retrofit.NewsAppApi;
import com.notmiyouji.newsapp.kotlin.NewsAppInterface;
import com.notmiyouji.newsapp.kotlin.Utils;
import com.notmiyouji.newsapp.kotlin.model.SignIn;
import com.notmiyouji.newsapp.kotlin.sharedsettings.SaveUserLogined;

import retrofit2.Call;
import retrofit2.Response;

public class GoogleMethod {
    NewsAppInterface newsAPPInterface = NewsAppApi.getAPIClient().create(NewsAppInterface.class);
    SaveUserLogined saveUserLogined;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private final AppCompatActivity activity;
    private final Button signInButton;
    private final Button signUpButton;

    public GoogleMethod(AppCompatActivity activity, Button signInButton, Button signUpButton) {
        this.activity = activity;
        this.signInButton = signInButton;
        this.signUpButton = signUpButton;
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        saveUserLogined = new SaveUserLogined(activity);
    }

    private GoogleSignInOptions getGso() {
        //if this line error, ignore it
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }

    public GoogleSignInClient getGoogleSignInClient() {
        return com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(activity, getGso());
    }

    public void firebaseAuthWithGoogle(String idToken, String email,
                                       String displayName, Uri avatarURL) {
        mAuth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(activity, task -> {
            if (task.isSuccessful()) {
                mAuth.getCurrentUser().updateProfile(new com.google.firebase.auth.UserProfileChangeRequest.Builder().
                                setDisplayName(displayName).
                                setPhotoUri(avatarURL)
                                .build())
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                user = mAuth.getCurrentUser();
                                assert user != null;
                                String fullName = user.getDisplayName();
                                String userToken = user.getUid();
                                //String username = "Google SSO";
                                //Google Avatar Image default so terrible, this line will fix it
                                String avatar = new FixBlurryGoogleImage(user.getPhotoUrl()).fixURL();
                                //User is not registered, save to database
                                //Always save to database with Google SSO Sign In
                                SavedToDatabase(fullName, email, userToken, fullName, avatar);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(activity, R.string.Error_login, Toast.LENGTH_SHORT).show();
                                signInButton.setEnabled(true);
                                signUpButton.setEnabled(true);
                            }
                        });
            }
        });
    }

    private void SavedToDatabase(String fullName, String email, String userToken, String userName, String avatar) {
        //First, save it to database
        Call<SignIn> callSSO = newsAPPInterface.signInWithGoogle(
                Utils.encodeToBase64(fullName),
                Utils.encodeToBase64(email),
                Utils.encodeToBase64(userToken),
                Utils.encodeToBase64(userName),
                Utils.encodeToBase64(avatar));
        assert callSSO != null;
        callSSO.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<SignIn> call, @NonNull Response<SignIn> response) {
                //Save successfully,
                //Now sign in
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        saveUserLogined.saveUserLogin(response.body().getUserId(),
                                response.body().getFullName(), response.body().getEmail(), userToken,
                                response.body().getNickName(), response.body().getAvatar(), "login");
                        //save birthday
                        if (response.body().getBirthday() != null) {
                            saveUserLogined.saveBirthday(response.body().getBirthday());
                        }
                        //save Gender
                        saveUserLogined.saveGender(response.body().getGender());
                        Toast.makeText(activity, R.string.sign_in_success, Toast.LENGTH_SHORT).show();
                        //If user login successfully, go to main activity
                        signInButton.setEnabled(true);
                        signUpButton.setEnabled(true);
                        activity.finish();
                        //restart application
                        Intent intent = activity.getPackageManager().getLaunchIntentForPackage(activity.getBaseContext().getPackageName());
                        assert intent != null;
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SignIn> call, @NonNull Throwable t) {
            }
        });
    }
}
