package com.example.GraduationThesis.Service.LazySingleton.Email;


public class EmailManager {
    private static EmailManager instance;
    private String email;

    private EmailManager() {
    }

    public static synchronized EmailManager getInstance() {
        if (instance == null) {
            instance = new EmailManager();
        }
        return instance;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void delete() {
        this.email = null;
    }
}
