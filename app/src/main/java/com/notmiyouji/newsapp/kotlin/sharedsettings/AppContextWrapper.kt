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
package com.notmiyouji.newsapp.kotlin.sharedsettings

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import java.util.Locale

class AppContextWrapper(base: Context?) : ContextWrapper(base) {
    companion object {
        @JvmStatic
        fun wrap(context: Context, language: String): ContextWrapper {
            val config = context.resources.configuration
            val sysLocale: Locale = getSystemLocale(config)
            if (language != "" && sysLocale.language != language) {
                val locale = Locale(language)
                Locale.setDefault(locale)
                setSystemLocale(config, locale)
            }
            context.createConfigurationContext(config)
            return AppContextWrapper(context)
        }

        private fun getSystemLocale(config: Configuration): Locale {
            return config.locales[0]
        }

        private fun setSystemLocale(config: Configuration, locale: Locale?) {
            config.setLocale(locale)
        }
    }
}