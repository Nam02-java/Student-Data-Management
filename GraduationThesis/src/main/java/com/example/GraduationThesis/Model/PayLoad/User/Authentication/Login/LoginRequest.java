package com.example.GraduationThesis.Model.PayLoad.User.Authentication.Login;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {

    private String username;

    private String password;

}
