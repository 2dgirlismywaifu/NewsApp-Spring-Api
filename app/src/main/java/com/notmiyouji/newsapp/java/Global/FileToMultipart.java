package com.notmiyouji.newsapp.java.Global;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileToMultipart {


    public MultipartBody.Part fileToMultipart(String path, String key) {
        File file = new File(path);
        RequestBody requestFile = RequestBody.Companion.create(file, MediaType.Companion.parse("image/*"));
        return MultipartBody.Part.createFormData(key, file.getName(), requestFile);
    }
}
