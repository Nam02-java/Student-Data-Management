package com.example.GraduationThesis.Model.PayLoad.User.Authentication.SignUp;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;
@Data
public class SignupRequest {

    @NotNull(message = "Email is null")
    @NotBlank(message = "Email is blank")
    private String email;

    private String numberPhone;
    private String userName;
    private String password;
    private Set<String> listRoles;
}

