package com.example.seminarfirstdemoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OldWayForResultActivity extends AppCompatActivity {
    private static final int REQUEST_PICK_IMAGE = 1;
    private static final int REQUEST_TAKE_PICTURE_PREVIEW = 2;

    private ImageView imageViewResult;
    private Button buttonPickImage, buttonTakePicturePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_way_for_result);

        imageViewResult = findViewById(R.id.imageViewResult);
        buttonPickImage = findViewById(R.id.buttonPickImage);
        buttonTakePicturePreview = findViewById(R.id.buttonTakePicturePreview);

        buttonPickImage.setOnClickListener(v -> {
            // Pick image from gallery
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_PICK_IMAGE);
        });

        buttonTakePicturePreview.setOnClickListener(v -> {
            // Take picture preview (thumbnail)
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_TAKE_PICTURE_PREVIEW);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imageViewResult.setImageURI(imageUri);
        } else if (requestCode == REQUEST_TAKE_PICTURE_PREVIEW && resultCode == RESULT_OK && data != null) {
            Bitmap thumbnail = data.getParcelableExtra("data");
            if (thumbnail != null) {
                imageViewResult.setImageBitmap(thumbnail);
            } else {
                Toast.makeText(this, "No image captured", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode != RESULT_OK) {
            Toast.makeText(this, "Action cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}