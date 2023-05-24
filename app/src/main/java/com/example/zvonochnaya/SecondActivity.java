package com.example.zvonochnaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SecondActivity extends AppCompatActivity {
    EditText text_name;
    EditText text_number;
    EditText text_email;
    EditText text_telegram;
    Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        text_name = (EditText) findViewById(R.id.editName);
        text_number = (EditText) findViewById(R.id.editNumber);
        text_email = (EditText) findViewById(R.id.editEmail);
        text_telegram = (EditText) findViewById(R.id.editTelegram);
        add = (Button) findViewById(R.id.buttonAdd);
        Button cancel = (Button) findViewById(R.id.buttonCancel);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = text_name.getText().toString();
                String number = text_number.getText().toString();
                String email = text_email.getText().toString();
                String telegram = text_telegram.getText().toString();
                Intent intentBack = new Intent();
                intentBack.putExtra("NAME", name);
                intentBack.putExtra("NUMBER", number);
                intentBack.putExtra("EMAIL", email);
                intentBack.putExtra("TELEGRAM", telegram);
                setResult(RESULT_OK, intentBack);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        text_name.addTextChangedListener(loginTextWatcher);
        text_email.addTextChangedListener(loginTextWatcher);
        text_number.addTextChangedListener(loginTextWatcher);
        text_telegram.addTextChangedListener(loginTextWatcher);
    }
    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String name = text_name.getText().toString().trim();
            String number = text_number.getText().toString().trim();
            String email = text_email.getText().toString().trim();
            add.setEnabled(!name.isEmpty() && !number.isEmpty() && !email.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
            String name = text_name.getText().toString().trim();
            String number = text_number.getText().toString().trim();
            String email = text_email.getText().toString().trim();
            add.setEnabled(!name.isEmpty() && !number.isEmpty() && !email.isEmpty());
        }
    };
}
