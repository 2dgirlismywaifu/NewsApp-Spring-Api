package com.notmiyouji.newsapp.java.SignInMethod;

import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.Signed.FixBlurryGoogleImage;
import com.notmiyouji.newsapp.kotlin.LoginedModel.CountSSO;
import com.notmiyouji.newsapp.kotlin.LoginedModel.SSO;
import com.notmiyouji.newsapp.kotlin.LoginedModel.UpdateSSO;
import com.notmiyouji.newsapp.kotlin.RetrofitInterface.NewsAPPInterface;
import com.notmiyouji.newsapp.kotlin.SharedSettings.SaveUserLogined;

import retrofit2.Call;
import retrofit2.Response;

public class GoogleMethod {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private GoogleSignInOptions gso;
    private AppCompatActivity activity;
    private GoogleSignInClient googleSignInClient;
    private Button signInButton, signUpButton;
    NewsAPPInterface newsAPPInterface = com.notmiyouji.newsapp.java.Retrofit.NewsAPPAPI.getAPIClient().create(NewsAPPInterface.class);
    SaveUserLogined saveUserLogined;

    public GoogleMethod(AppCompatActivity activity, Button signInButton, Button signUpButton) {
        this.activity = activity;
        this.signInButton = signInButton;
        this.signUpButton = signUpButton;
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        saveUserLogined = new SaveUserLogined(activity);
    }

    private GoogleSignInOptions getGso() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        return gso;
    }

    public GoogleSignInClient getGoogleSignInClient() {
        googleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(activity, getGso());
        return googleSignInClient;
    }

    public void firebaseAuthWithGoogle(String idToken, String email) {
        mAuth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        user = mAuth.getCurrentUser();
                        assert user != null;
                        String fullname = user.getDisplayName();
                        String uid = user.getUid();
                        String getEmail =email;
                        //String username = "Google SSO";
                        //Google Avatar Image default so terrible, this line will fix it
                        String avatar = new FixBlurryGoogleImage(user.getPhotoUrl()).fixURL();
                        //User is not registered, save to database
                        //Always save to database with Google SSO Sign In
                        CheckSSOAccount(fullname, getEmail, uid, fullname, avatar);
                        //User is already registered, save to shared settings
                        saveUserLogined.saveUserLogined(uid, fullname, email, uid, fullname, avatar,"google");
                        Toast.makeText(activity, R.string.sign_in_success, Toast.LENGTH_SHORT).show();
                        //If user login successfully, go to main activity
                        signInButton.setEnabled(true);
                        signUpButton.setEnabled(true);
                        activity.finish();
                        //restart application
                        Intent intent = activity.getPackageManager().getLaunchIntentForPackage(activity.getBaseContext().getPackageName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(activity, R.string.Error_login, Toast.LENGTH_SHORT).show();
                        signInButton.setEnabled(true);
                        signUpButton.setEnabled(true);
                    }
        });
    }

    private void CheckSSOAccount(String displayName, String email, String uid, String username, String url) {
        Call<CountSSO> callCountSSO = newsAPPInterface.ssoCount(email);
        callCountSSO.enqueue(new retrofit2.Callback<CountSSO>() {
            @Override
            public void onResponse(Call<CountSSO> call, Response<CountSSO> response) {
                if (response.isSuccessful()) {
                    CountSSO countSSO = response.body();
                    if (countSSO.getStatus().equals("fail")) {
                        //Save to database
                        SavedToDatabase(displayName, email, uid, username, url);
                    }
                    else {
                        //We need always update information when user use SSO
                        UpdateSSO(countSSO.getUserId(), displayName, url);
                        //save birthday
                        saveUserLogined.saveBirthday(countSSO.getBirthday());
                        //save Gender
                        saveUserLogined.saveGender(countSSO.getGender());

                    }
                }
            }
            @Override
            public void onFailure(Call<CountSSO> call, Throwable t) {
                //if return 500, it means that account not in database
            }
        });
    }

    private void SavedToDatabase(String fullname, String email, String uid, String username, String avatar) {
        //First, save it to database
        Call<SSO> callSSO = newsAPPInterface.sso(fullname, email, username, avatar);
        callSSO.enqueue(new retrofit2.Callback<SSO>() {
            @Override
            public void onResponse(Call<SSO> call, Response<SSO> response) {
                //Save successfully,
            }
            @Override
            public void onFailure(Call<SSO> call, Throwable t) {
            }
        });
    }

    private void UpdateSSO(String user_id, String name, String avatar) {
        Call<UpdateSSO> callUpdateSSO = newsAPPInterface.updateSSO(user_id, name, avatar);
        callUpdateSSO.enqueue(new retrofit2.Callback<UpdateSSO>() {
            @Override
            public void onResponse(Call<UpdateSSO> call, Response<UpdateSSO> response) {
                if (response.isSuccessful()) {
                    UpdateSSO updateSSO = response.body();
                    if (updateSSO.getStatus().equals("pass")) {
                        //Update successfully
                    }
                }
            }
            @Override
            public void onFailure(Call<UpdateSSO> call, Throwable t) {
            }
        });
    }
}
