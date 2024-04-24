package com.example.GraduationThesis.Service.LazySingleton.Password;


public class PasswordManager {
    private static PasswordManager instance;
    private String password;

    private PasswordManager() {
    }

    public static synchronized PasswordManager getInstance() {
        if (instance == null) {
            instance = new PasswordManager();
        }
        return instance;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void delete() {
        password = null;
    }
}

