package com.example.zvonochnaya;

import java.util.HashMap;

public class Contact extends HashMap<String, String> {
    public static final String NAME = "name";
    public static final String PHONE = "phone";
    public static final String EMAIl = "email";
    public static final String TELEGRAM = "telegram";
    public Contact(String name, String phone, String email, String telegram) {
        super();
        super.put(NAME, name);
        super.put(PHONE, phone);
        super.put(EMAIl, email);
        super.put(TELEGRAM, telegram);
    }

    public String getName() {
        return super.get(NAME);
    }
    public String getTelegram() {return super.get(TELEGRAM);}
    public String getPhone() {
        return super.get(PHONE);
    }
    public String getEmail() {
        return super.get(EMAIl);
    }
    public void setName(String name) {
        super.put(NAME, name);
    }
    public void setPhone(String phone) {
        super.put(PHONE, phone);
    }
    public void setEmail(String email) {
        super.put(EMAIl, email);
    }
    public void setTelegram(String telegram) {super.put(TELEGRAM, telegram);}
}
