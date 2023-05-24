package com.example.zvonochnaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SeventhActivity extends AppCompatActivity {
    Button edit;
    ImageView back;
    TextView Name, Number, Email, Telegram;
    String index;
    Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seventh);
        back = (ImageView) findViewById(R.id.buttonBack);
        edit = (Button) findViewById(R.id.buttonEdit);
        extras = getIntent().getExtras();
        Name = (TextView) findViewById(R.id.setName);
        Number = (TextView) findViewById(R.id.setNumber);
        Email = (TextView) findViewById(R.id.setEmail);
        Telegram = (TextView) findViewById(R.id.setTelegram);
        Name.setText(extras.getString("NAME"));
        Number.setText(extras.getString("NUMBER"));
        Email.setText(extras.getString("EMAIL"));
        Telegram.setText(extras.getString("TELEGRAM"));
        index = extras.getString("INDEX");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
