package com.example.GraduationThesis.Controller.APICollection.AdminAndUser.Authentication;

import com.example.GraduationThesis.Model.PayLoad.User.Authentication.Login.LoginRequest;
import com.example.GraduationThesis.Model.PayLoad.User.Authentication.Login.LoginResponse;
import com.example.GraduationThesis.Service.API.InterfaceService.AdminAndUser.Authentication.AuthenticationServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/public/auth")
public class LoginAPI {

    @Autowired
    @Qualifier("AuthenticationImplementation")
    private AuthenticationServiceAPI authenticationServiceAPI;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return authenticationServiceAPI.login(loginRequest);
    }
}
