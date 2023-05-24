package com.example.zvonochnaya;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ThirdActivity extends AppCompatActivity implements View.OnClickListener {

    EditText textName;
    EditText textNumber;
    EditText textEmail;
    EditText textTelegram;
    int index;
    String curr_name;
    String curr_number;
    String curr_email;
    String curr_telegram;
    Button edit, delete, cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        Bundle extras = getIntent().getExtras();
        edit = (Button) findViewById(R.id.buttonEdit);
        delete = (Button) findViewById(R.id.buttonDelete);
        cancel = (Button) findViewById(R.id.buttonCancel);
        curr_name = extras.getString("NAME");
        curr_number = extras.getString("NUMBER");
        curr_email = extras.getString("EMAIL");
        curr_telegram = extras.getString("TELEGRAM");
        index = extras.getInt("INDEX");
        textName = (EditText) findViewById(R.id.editName);
        textNumber = (EditText) findViewById(R.id.editNumber);
        textEmail = (EditText) findViewById(R.id.editEmail);
        textTelegram = (EditText) findViewById(R.id.editTelegram);
        textName.setText(curr_name);
        textNumber.setText(curr_number);
        textEmail.setText(curr_email);
        textTelegram.setText(curr_telegram);
        edit.setOnClickListener(this);
        delete.setOnClickListener(this);
        cancel.setOnClickListener(this);
        textName.addTextChangedListener(loginTextWatcher);
        textEmail.addTextChangedListener(loginTextWatcher);
        textNumber.addTextChangedListener(loginTextWatcher);
        textTelegram.addTextChangedListener(loginTextWatcher);
    }
    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String name = textName.getText().toString().trim();
            String number = textNumber.getText().toString().trim();
            String email = textEmail.getText().toString().trim();
            String telegram = textTelegram.getText().toString().trim();
            edit.setEnabled(!name.isEmpty() && !number.isEmpty() && !email.isEmpty() );
        }

        @Override
        public void afterTextChanged(Editable s) {
            String name = textName.getText().toString().trim();
            String number = textNumber.getText().toString().trim();
            String email = textEmail.getText().toString().trim();
            String telegram = textTelegram.getText().toString().trim();
            edit.setEnabled(!name.isEmpty() && !number.isEmpty() && !email.isEmpty()  );
        }
    };
    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.buttonCancel:
                finish();
                break;
            case R.id.buttonDelete:
                AlertDialog.Builder builder = new AlertDialog.Builder(ThirdActivity.this);
                builder.setTitle("Вы уверены?")
                        .setPositiveButton("Да",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent i = new Intent();
                                        i.putExtra("CHECK", '1');
                                        setResult(RESULT_OK, i);
                                        finish();
                                    }
                                })
                        .setNegativeButton("Нет",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            case R.id.buttonEdit:
                String tmpName = (textName.getText()).toString();
                String tmpNumber = (textNumber.getText()).toString();
                String tmpEmail = (textEmail.getText()).toString();
                String tmpTelegram = (textTelegram.getText()).toString();
                i = new Intent();
                i.putExtra("NAME", tmpName);
                i.putExtra("NUMBER", tmpNumber);
                i.putExtra("EMAIL", tmpEmail);
                i.putExtra("TELEGRAM", tmpTelegram);
                i.putExtra("CHECK", '0');
                setResult(RESULT_OK, i);
                finish();
                break;
        }
    }
}