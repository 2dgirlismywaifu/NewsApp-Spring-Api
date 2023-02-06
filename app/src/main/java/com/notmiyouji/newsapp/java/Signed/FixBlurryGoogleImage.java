package com.notmiyouji.newsapp.java.Signed;

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
