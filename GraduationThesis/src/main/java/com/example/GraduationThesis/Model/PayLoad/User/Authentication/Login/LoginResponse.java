package com.example.GraduationThesis.Model.PayLoad.User.Authentication.Login;

import com.example.GraduationThesis.Service.Utils.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String email;
    private String numberPhone;
    private String userName;
    private String listRole;
    private String status;

}
