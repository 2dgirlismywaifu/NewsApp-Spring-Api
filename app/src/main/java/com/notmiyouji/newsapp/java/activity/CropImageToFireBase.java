package com.notmiyouji.newsapp.java.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.canhub.cropper.CropImageView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.activity.userlogin.UpdateInformation;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;
import com.notmiyouji.newsapp.kotlin.sharedsettings.GetUserLogin;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class CropImageToFireBase extends AppCompatActivity {
    private CropImageView cropImageView;
    private GetUserLogin getUserLogin;
    private UpdateInformation updateInformation;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        getUserLogin = new GetUserLogin(this);
        updateInformation = new UpdateInformation(getUserLogin.getUserID(), this);
        cropImageView = findViewById(R.id.cropImageViewer);
        //Get the image uri from CropImageActivity
        Uri imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));
        cropImageView.setImageUriAsync(imageUri);
        Button cropButton = findViewById(R.id.cropButton);
        cropButton.setOnClickListener(v -> {
            Bitmap cropped = cropImageView.getCroppedImage();
            // Convert the Bitmap to a byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            assert cropped != null;
            cropped.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] data = byteArrayOutputStream.toByteArray();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference riversRef = storageRef.child("avatar/" + "avatar-" + getUserLogin.getUserID() + ".jpg");
            UploadTask uploadTask = riversRef.putBytes(data);
            uploadTask.addOnSuccessListener(taskSnapshot -> riversRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                updateInformation.updateAvatar(uri1.toString());
                //End the activity
                finish();
            })).addOnFailureListener(exception -> {
                // Handle unsuccessful uploads
                // Log the error message
                Log.e("Firebase Storage", "Upload failed: " + exception.getMessage());
                // ...
            });
        });
    }
}