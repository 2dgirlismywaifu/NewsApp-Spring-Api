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

package com.notmiyouji.newsapp.java.SharedSettings;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

public class LanguagePrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // Shared preferences file name
    public LanguagePrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences("LangCode", MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setLocal(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        _context.getResources().updateConfiguration(configuration, _context.getResources().getDisplayMetrics());
        //Save config by shared preferences
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("lang", lang);
        editor.apply();
    }

    public String getLang() {
        return pref.getString("lang", "");
    }

    public void loadLocal() {
        setLocal(getLang());
    }
}
