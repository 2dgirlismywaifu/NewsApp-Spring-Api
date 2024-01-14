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

package com.notmiyouji.newsapp.java.userlogin;

import android.net.Uri;

public class FixBlurryGoogleImage {
    public Uri url;

    public FixBlurryGoogleImage(Uri url) {
        this.url = url;
    }

    public String fixURL() {
        //Fix blurry image from google
        // Variable holding the original String portion of the url that will be replaced
        String originalPieceOfUrl = "s96-c";

        // Variable holding the new String portion of the url that does the replacing, to improve image quality
        String newPieceOfUrlToAdd = "s400-c";

        // Check if the Url path is null
        String newString = null;
        if (url != null) {

            // Convert the Url to a String and store into a variable
            String photoPath = url.toString();

            // Replace the original part of the Url with the new part
            newString = photoPath.replace(originalPieceOfUrl, newPieceOfUrlToAdd);

        }
        return newString;
    }
}
