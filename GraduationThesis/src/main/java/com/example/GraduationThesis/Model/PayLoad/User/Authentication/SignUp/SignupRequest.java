package com.example.GraduationThesis.Model.PayLoad.User.Authentication.SignUp;

import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {

    private String email;
    private String numberPhone;
    private String userName;
    private String password;
    private Set<String> listRoles;
}

