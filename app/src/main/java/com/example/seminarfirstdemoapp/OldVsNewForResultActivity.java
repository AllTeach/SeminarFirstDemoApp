package com.example.seminarfirstdemoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OldVsNewForResultActivity extends AppCompatActivity {


    private static final int REQUEST_CODE_TAKE_PICTURE = 100;
    private ImageView imageViewResult;

    private ActivityResultLauncher<Void> takePictureLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_old_vs_new_for_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        setUI();

        takePictureLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), bitmap -> {
            if (bitmap != null) {
                imageViewResult.setImageBitmap(bitmap);
            } else {
                // Handle the case where no image was captured
            }
        });






    }

    private void setUI() {
        // ORIGINAL WAY
        imageViewResult = findViewById(R.id.imageViewResultBoth);
        Button buttonPictureOld = findViewById(R.id.buttonTakePictureOld);
        Button buttonPictureNew = findViewById(R.id.buttonTakePictureNew);

        buttonPictureOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureOriginalWay();

            }
        });

        buttonPictureNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureLauncher.launch(null);
            }
        });
    }

    private void takePictureOriginalWay() {

        // 1 create implicit intent to open camera
        // 2 start activity for result with request code
        // 3 implement onActivity Result to handle the result
        // 4 extract image from intent and place it in imageViewResult
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_CODE_TAKE_PICTURE);
        
    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

        // check the request
        // check the result code
        if(requestCode== REQUEST_CODE_TAKE_PICTURE)
        {
            if(resultCode==RESULT_OK) // we have data!
            {
                // extract the image from the intent
                // and place it in the imageViewResult
                Bitmap bitmap = data.getParcelableExtra("data");
                imageViewResult.setImageBitmap(bitmap);
            }
        }
    }
}