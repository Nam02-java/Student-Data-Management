package com.example.GraduationThesis.Model.PayLoad.User.Authentication.SignUp;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {

    @Email
    @NotBlank(message = "Email is not empty or null")
    private String email;

    @Size(min = 10, max = 11, message = "Number phone must have 10 to 11 digits")
    @Pattern(regexp = "\\d+", message = "Number phone must contain only digits")
    @NotBlank(message = "Number is not empty or null")
    private String numberPhone;

    @Size(min = 6 , max = 30, message = "User Name must have 6 to 30 characters")
    @NotBlank(message = "User Name not empty or null")
    private String userName;

    @Size(min = 8, message = "Password minimum 8 characters")
    @NotBlank(message = "Password is not empty or null")
    private String password;

    private Set<String> listRoles;
}

