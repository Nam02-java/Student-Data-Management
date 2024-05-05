package com.example.GraduationThesis.Controller.APICollection.AdminAndUser.Authentication;

import com.example.GraduationThesis.Model.PayLoad.User.Authentication.SignUp.SignupRequest;
import com.example.GraduationThesis.Service.API.InterfaceService.AdminAndUser.Authentication.AuthenticationServiceAPI;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/v1/public/auth")
public class SignUpAPI {

    @Autowired
    @Qualifier("AuthenticationImplementation")
    AuthenticationServiceAPI authenticationServiceAPI;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {
        return authenticationServiceAPI.signUp(signupRequest);
    }
}
