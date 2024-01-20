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

package com.notmiyouji.newsapp.java.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.notmiyouji.newsapp.R;

public class MaterialAltertLoading {
    AppCompatActivity activity;
    MaterialAlertDialogBuilder builder;

    public MaterialAltertLoading(AppCompatActivity activity) {
        this.activity = activity;
    }

    public MaterialAlertDialogBuilder getDialog() {
        if (builder == null) {
            builder = new MaterialAlertDialogBuilder(activity);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle(activity.getString(R.string.app_name));
            //builder.setCancelable(false);
            builder.setView(R.layout.show_loading_bar);
        }
        return builder;
    }

    public MaterialAlertDialogBuilder firebaseUploadImage() {
        if (builder == null) {
            builder = new MaterialAlertDialogBuilder(activity);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle(activity.getString(R.string.app_name));
            builder.setCancelable(false);
            builder.setView(R.layout.fire_base_upload);
        }
        return builder;
    }
}
