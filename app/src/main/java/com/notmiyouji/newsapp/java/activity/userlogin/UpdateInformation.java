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

package com.notmiyouji.newsapp.java.activity.userlogin;

import static com.notmiyouji.newsapp.kotlin.retrofit.NewsAppApi.getApiClient;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.retrofit.NewsAppInterface;
import com.notmiyouji.newsapp.kotlin.util.AppUtils;
import com.notmiyouji.newsapp.kotlin.model.user.UserInformation;
import com.notmiyouji.newsapp.kotlin.model.user.VerifyNickNameEmail;
import com.notmiyouji.newsapp.kotlin.sharedsettings.SaveUserLogin;

import java.util.Base64;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

public class UpdateInformation {
    private final String userId;
    private final AppCompatActivity activity;
    NewsAppInterface newsAPPInterface = getApiClient().create(NewsAppInterface.class);

    public UpdateInformation(String userId, AppCompatActivity activity) {
        this.activity = activity;
        this.userId = userId;
    }

    public void updateAvatar(String avatar) {
        Call<UserInformation> call = newsAPPInterface.updateUserInformation
                (AppUtils.encodeToBase64(userId), "", "", "", "", AppUtils.encodeToBase64(avatar));
        assert call != null;
        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserInformation> call, @NonNull retrofit2.Response<UserInformation> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        SaveUserLogin saveUserLogin = new SaveUserLogin(activity);
                        saveUserLogin.saveAvatar(new String(Base64.getDecoder().decode(response.body().getAvatar())));
                        Toast.makeText(activity, R.string.update_avatar_image_success, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserInformation> call, @NonNull Throwable t) {
                Toast.makeText(activity, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateBirthday(String birthday) {
        Call<UserInformation> call = newsAPPInterface.updateUserInformation
                (AppUtils.encodeToBase64(userId), "", "", AppUtils.encodeToBase64(birthday), "", "");
        assert call != null;
        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserInformation> call, @NonNull retrofit2.Response<UserInformation> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        SaveUserLogin saveUserLogin = new SaveUserLogin(activity);
                        saveUserLogin.saveBirthday(new String(Base64.getDecoder().decode(response.body().getBirthday())));
                        Toast.makeText(activity, R.string.birthday_updated, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserInformation> call, @NonNull Throwable t) {
                Toast.makeText(activity, R.string.birthday_updated_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateFullName(String fullName) {
        Call<UserInformation> call = newsAPPInterface.updateUserInformation
                (AppUtils.encodeToBase64(userId), "", AppUtils.encodeToBase64(fullName), "", "", "");
        assert call != null;
        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserInformation> call, @NonNull retrofit2.Response<UserInformation> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        SaveUserLogin saveUserLogin = new SaveUserLogin(activity);
                        saveUserLogin.saveFullname(new String(Base64.getDecoder().decode(response.body().getFullName())));
                        Toast.makeText(activity, R.string.fullname_updated, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserInformation> call, @NonNull Throwable t) {
                Toast.makeText(activity, R.string.fullname_already_exists, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateGender(String gender) {
        //Retrofit call update gender request
        Call<UserInformation> call = newsAPPInterface.updateUserInformation
                (AppUtils.encodeToBase64(userId), "", "", "", AppUtils.encodeToBase64(gender), "");
        assert call != null;
        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserInformation> call, @NonNull retrofit2.Response<UserInformation> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        SaveUserLogin saveUserLogin = new SaveUserLogin(activity);
                        saveUserLogin.saveGender(new String(Base64.getDecoder().decode(response.body().getGender())));
                        Toast.makeText(activity, R.string.gender_updated, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserInformation> call, @NonNull Throwable t) {
                Toast.makeText(activity, R.string.gender_updated_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkUserName(String userName, String email) {
        Call<VerifyNickNameEmail> call = newsAPPInterface.verifyNickNameAndEmail(AppUtils.encodeToBase64(userName), AppUtils.encodeToBase64(email));
        assert call != null;
        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<VerifyNickNameEmail> call, @NonNull Response<VerifyNickNameEmail> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (Objects.equals(response.body().getNickname(), userName)) {
                            Toast.makeText(activity, R.string.username_already_exists, Toast.LENGTH_SHORT).show();
                        } else {
                            updateUserName(userName);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<VerifyNickNameEmail> call, @NonNull Throwable t) {
            }
        });
    }

    public void updateUserName(String userName) {
        //Retrofit call update username request
        Call<UserInformation> call = newsAPPInterface.updateUserInformation
                (AppUtils.encodeToBase64(userId), AppUtils.encodeToBase64(userName), "", "", "", "");
        assert call != null;
        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserInformation> call, @NonNull retrofit2.Response<UserInformation> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        SaveUserLogin saveUserLogin = new SaveUserLogin(activity);
                        saveUserLogin.saveUsername(new String(Base64.getDecoder().decode(response.body().getUserName())));
                        Toast.makeText(activity, R.string.username_updated, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserInformation> call, @NonNull Throwable t) {
                Toast.makeText(activity, R.string.username_already_exists, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void updatePassword(String email, String userToken) {
        //Retrofit call update password request
        Call<UserInformation> updatePasswordNow = newsAPPInterface.changeUserToken(AppUtils.encodeToBase64(userId),
                AppUtils.encodeToBase64(email), AppUtils.encodeToBase64(userToken));
        assert updatePasswordNow != null;
        updatePasswordNow.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserInformation> call, @NonNull retrofit2.Response<UserInformation> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Toast.makeText(activity, R.string.password_updated, Toast.LENGTH_SHORT).show();
                        SaveUserLogin saveUserLogin = new SaveUserLogin(activity);
                        saveUserLogin.saveUserLogin("", "", "", "", "", "", "");
                        saveUserLogin.saveBirthday("");
                        saveUserLogin.saveGender("");
                    } else {
                        Toast.makeText(activity, R.string.password_updated_failed, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserInformation> call, @NonNull Throwable t) {
                Toast.makeText(activity, R.string.password_updated_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
