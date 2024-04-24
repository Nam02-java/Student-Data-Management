package com.example.GraduationThesis.Service.LazySingleton.NumberPhone;

public class NumberPhoneManager {
    private static NumberPhoneManager instance;
    private String numberphone;

    private NumberPhoneManager() {
    }

    public static synchronized NumberPhoneManager getInstance() {
        if (instance == null) {
            instance = new NumberPhoneManager();
        }
        return instance;
    }

    public void setNumberphone(String numberphone) {
        this.numberphone = numberphone;
    }

    public String getNumberphone() {
        return numberphone;
    }

    public void delete() {
        this.numberphone = null;
    }
}
