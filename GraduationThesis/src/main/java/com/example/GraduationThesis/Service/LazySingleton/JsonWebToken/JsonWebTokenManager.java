package com.example.GraduationThesis.Service.LazySingleton.JsonWebToken;

public class JsonWebTokenManager {
    private static JsonWebTokenManager instance;
    private String jwtToken;

    private JsonWebTokenManager() {
    }

    public static synchronized JsonWebTokenManager getInstance() {
        if (instance == null) {
            instance = new JsonWebTokenManager();
        }
        return instance;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void delete() {
        this.jwtToken = null;
    }
}

