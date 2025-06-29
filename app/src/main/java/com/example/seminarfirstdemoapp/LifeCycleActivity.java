package com.example.seminarfirstdemoapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LifeCycleActivity extends AppCompatActivity {

    private static final String TAG = "LifecycleActivity";
    private static final String KEY_INPUT = "input_string";

    private EditText editText;
    private String inputString = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_cycle);
        Log.d(TAG, "onCreate: entering onCreate");
        Log.d(TAG, "onCreate: value of inputString=" + inputString);


        editText = findViewById(R.id.editText);
        Button buttonNext = findViewById(R.id.buttonNext);
        Button buttonNextFinish = findViewById(R.id.buttonNextFinish);
        Button buttonDialog = findViewById(R.id.buttonDialog);

        // Restore state if available
        if (savedInstanceState != null) {
   //         inputString = savedInstanceState.getString(KEY_INPUT, "");
            Log.d(TAG, "onCreate: Restored inputString=" + inputString);
         //   editText.setText(inputString);
        }

        buttonNext.setOnClickListener(v -> {
            inputString = editText.getText().toString();
            Intent intent = new Intent(LifeCycleActivity.this, FruitActivity.class);
            intent.putExtra("input", inputString);
            startActivity(intent); // This activity remains in the back stack
        });

        buttonNextFinish.setOnClickListener(v -> {
            inputString = editText.getText().toString();
            Intent intent = new Intent(LifeCycleActivity.this, FruitActivity.class);
            intent.putExtra("input", inputString);
            startActivity(intent);
            finish(); // This activity will be destroyed
        });

        buttonDialog.setOnClickListener(v -> showCustomDialog());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       inputString = editText.getText().toString();
        outState.putString(KEY_INPUT, inputString);
        Log.d(TAG, "onSaveInstanceState: Saved inputString=" + inputString);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        inputString = savedInstanceState.getString(KEY_INPUT, "");
        Log.d(TAG, "onRestoreInstanceState: Restored inputString=" + inputString);
        editText.setText(inputString);
    }



    private void showCustomDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Dialog Example")
                .setMessage("This is a dialog. The activity is still running and in the foreground!")
                .setPositiveButton("OK", null)
                .show();
    }
}