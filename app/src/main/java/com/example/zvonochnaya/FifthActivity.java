package com.example.zvonochnaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class FifthActivity extends AppCompatActivity {

    Button send;
    ImageView back;
    EditText theme, message;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);
        Bundle extras = getIntent().getExtras();
        name = extras.getString("EMAIL");
        back = (ImageView) findViewById(R.id.buttonBack);
        theme = findViewById(R.id.editTheme);
        message = findViewById(R.id.editText);
        send = findViewById(R.id.buttonSend);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String thm = theme.getText().toString();
                String msg = message.getText().toString();
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{name});
                email.putExtra(Intent.EXTRA_SUBJECT, thm);
                email.putExtra(Intent.EXTRA_TEXT, msg);
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Выберите приложение :"));
                finish();
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
