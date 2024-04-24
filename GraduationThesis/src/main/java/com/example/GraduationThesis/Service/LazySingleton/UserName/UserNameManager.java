package com.example.GraduationThesis.Service.LazySingleton.UserName;


public class UserNameManager {

    private static UserNameManager instance;
    private String username;

    private UserNameManager() {
    }

    public static synchronized UserNameManager getInstance() {
        if (instance == null) {
            instance = new UserNameManager();
        }
        return instance;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void delete() {
        this.username = null;
    }
}

