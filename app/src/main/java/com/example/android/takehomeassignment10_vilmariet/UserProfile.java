package com.example.android.takehomeassignment10_vilmariet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class UserProfile extends AppCompatActivity {

    private StorageReference mStorageRef;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private Button uploadArtButton, captureArButton;
    private ImageView pictureBox;

    private Uri uri;
    private Bitmap my_image;


    private static final int CAMERA_REQUEST_CODE = 93;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mStorageRef = FirebaseStorage.getInstance().getReference();


        uploadArtButton = findViewById(R.id.upload_art_button);
        captureArButton = findViewById(R.id.capture_art_button);
        pictureBox = findViewById(R.id.image);

        captureArButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                chooseImage();

            }
        });

        uploadArtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

    }

    private void uploadImage() {

        if (uri != null) {


            StorageReference ref = storage.getReference().child("et_coffee.jpg");
            try {
                final File localFile = File.createTempFile("Photos", "jpg");
                ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        my_image = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        pictureBox.setImageBitmap(my_image);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                pictureBox.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


            StorageReference filepath = mStorageRef.child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(UserProfile.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }


            });


        }
    }
}

