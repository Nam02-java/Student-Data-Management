package com.example.GraduationThesis.Service.Utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Status {
    LOGIN_SUCCESSFUL("Login Succesful"),
    LOGIN_FAILED_WRONG_USERNAME("Wrong username"),

    LOGIN_FAILED_WRONG_PASSWORD("Wrong password");

    @Override
    public String toString() {
        return value;
    }

    private final String value;
}
