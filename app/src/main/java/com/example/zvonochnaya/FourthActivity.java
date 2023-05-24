package com.example.zvonochnaya;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class FourthActivity extends AppCompatActivity{
    String text;
    EditText message;
    ImageView back;
    Button send;
    private static final int READ_SMS_PERMISSIONS_REQUEST = 4;
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToSendSMS(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if ((Boolean) shouldShowRequestPermissionRationale(
                    Manifest.permission.SEND_SMS)) {
                Toast.makeText(this, "Пожалуйста, предоставьте доступ!", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                    READ_SMS_PERMISSIONS_REQUEST);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);
        getPermissionToSendSMS();
        send = (Button) findViewById(R.id.send);
        back = (ImageView) findViewById(R.id.back);
        message = (EditText)findViewById(R.id.messageText);
        Bundle extras = getIntent().getExtras();
        text = extras.getString("NUMBER");
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toSms = "smsto:" + text;
                String messageText = message.getText().toString();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(toSms, null, messageText, null, null);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "SMS sent!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
