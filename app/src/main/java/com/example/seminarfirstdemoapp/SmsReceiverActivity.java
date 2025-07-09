package com.example.seminarfirstdemoapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SmsReceiverActivity extends AppCompatActivity {

    private BroadcastReceiver dynamicReceiver;
    private SmsBroadcastReceiver smsBroadcastReceiver;

    private final ActivityResultLauncher<String> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, "SMS Permission Granted", Toast.LENGTH_SHORT).show();
                    // perform action
                } else {
                    Toast.makeText(this, "SMS Permission Denied", Toast.LENGTH_SHORT).show();
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sms_receiver);


        // check if permission is already granted
        // if granted = perform action
        // if not granted = request permission
        // if user allows permission, perform action



        // Request SMS permission
        // This is a runtime permission request for Android 6.0 (API level 23) and above
        checkPermissions();


    //    checkMicrophonePermission();

        setUI();
    }

    private void checkMicrophonePermission() {
        if(checkSelfPermission(Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED)
        {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        } else {
            Toast.makeText(this, "Microphone Permission Already Granted", Toast.LENGTH_SHORT).show();
            // perform the action
        }
    }

    private void setUI() {
     //   Button btnRegisterDynamic = findViewById(R.id.btnRegisterDynamic);
     //   Button btnUnregisterDynamic = findViewById(R.id.btnUnregisterDynamic);
    }

    private void checkPermissions() {
            if (checkSelfPermission(android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(android.Manifest.permission.RECEIVE_SMS);
            } else {
                Toast.makeText(this, "SMS Permission Already Granted", Toast.LENGTH_SHORT).show();
                // perform the action
            }
        }

    private void registerDynamicReceiver() {
            dynamicReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
                        Bundle bundle = intent.getExtras();
                        if (bundle != null) {
                            Object[] pdus = (Object[]) bundle.get("pdus");
                            if (pdus != null) {
                                for (Object pdu : pdus) {
                                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
                                    String message = sms.getMessageBody();
                                    Toast.makeText(context, "Dynamic SMS: " + message, Toast.LENGTH_SHORT).show();
                                    Log.d("BR", "onReceive: " + message);
                                }
                            }
                        }
                    }
                }
            };
            IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
            registerReceiver(dynamicReceiver, filter);
            Toast.makeText(this, "Dynamic Receiver Registered", Toast.LENGTH_SHORT).show();
        }


    private void unregisterDynamicReceiver() {
        if ( dynamicReceiver != null) {
            unregisterReceiver(dynamicReceiver);
            Toast.makeText(this, "Dynamic Receiver Unregistered", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerDynamicBroadcastReceiver();
        // Register the dynamic receiver when the activity is started
   //     registerDynamicReceiver();
    }

    private void registerDynamicBroadcastReceiver() {
        if(smsBroadcastReceiver==null)
        {
            smsBroadcastReceiver = new SmsBroadcastReceiver();
            // Register the receiver for SMS_RECEIVED action
            IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
            registerReceiver(smsBroadcastReceiver, filter);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unregister the dynamic receiver when the activity is stopped
 //       unregisterDynamicReceiver();

        unRegisterBroadcastReceiver();
    }

    private void unRegisterBroadcastReceiver() {

        if(smsBroadcastReceiver != null) {
            unregisterReceiver(smsBroadcastReceiver);
            smsBroadcastReceiver = null; // Clear the reference to avoid memory leaks
            Toast.makeText(this, "Broadcast Receiver Unregistered", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
           }
}