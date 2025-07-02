package com.example.seminarfirstdemoapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;

public class ResultsActivity extends AppCompatActivity {

    private ImageView imageViewCamera;
    private ImageView imageViewGallery;

    private ActivityResultLauncher<Void> takePicturePreviewLauncher;
    private ActivityResultLauncher<String> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        imageViewCamera = findViewById(R.id.imageViewCamera);
        imageViewGallery = findViewById(R.id.imageViewGallery);

        // Register for camera thumbnail
        takePicturePreviewLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicturePreview(),
                new ActivityResultCallback<Bitmap>() {
                    @Override
                    public void onActivityResult(Bitmap result) {
                        if (result != null) {
                            imageViewCamera.setImageBitmap(result);
                            String filePath = saveBitmapToInternalStorage(result, "my_camera_image");

                        } else {
                            Toast.makeText(ResultsActivity.this, "No image captured", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Register for gallery image
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            try {
                                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                                imageViewGallery.setImageBitmap(bitmap);
                            } catch (Exception e) {
                                Toast.makeText(ResultsActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ResultsActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        imageViewCamera.setOnClickListener(v -> takePicturePreviewLauncher.launch(null));
        imageViewGallery.setOnClickListener(v -> pickImageLauncher.launch("image/*"));
    }


    public String saveBitmapToInternalStorage(Bitmap bitmap, String filename) {
        try {
            // "openFileOutput" creates a private file associated with your app
            FileOutputStream fos = openFileOutput(filename, MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            return getFileStreamPath(filename).getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}