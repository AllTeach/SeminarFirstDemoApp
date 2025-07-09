package com.example.seminarfirstdemoapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        // You can extract the SMS data from the intent and handle it here.
        // For example, you can log the SMS content or show a notification.
        String action = intent.getAction();
        if ("android.provider.Telephony.SMS_RECEIVED".equals(action)) {
            // Extract SMS data from the intent
            // This is where you would handle the received SMS
            // For example, you can log it or show a notification
            // please read the message from the intent
            // Example: Log the SMS content
            Log.d("SmsBroadcastReceiver", "SMS received");
            Toast.makeText(context, "SMS received", Toast.LENGTH_SHORT).show();
      //      extractSMSData(intent);
    }
}

    private void extractSMSData(Intent intent) {

        Log.d("SmsBroadcastReceiver", "Extracting SMS data...");
        // Example: Log the sender and message content
         SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
         for (SmsMessage message : messages) {
             String sender = message.getDisplayOriginatingAddress();
             String content = message.getMessageBody();
             Log.d("SmsBroadcastReceiver", "Sender: " + sender + ", Content: " + content);
         }
    }
    }