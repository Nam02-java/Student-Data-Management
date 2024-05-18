package com.example.GraduationThesis.Service.API.ServiceImplenments.AdminAndUser.Authentication;

import com.example.GraduationThesis.Controller.SringSecurity6.JsonWebToken.JsonWebTokenProvider;
import com.example.GraduationThesis.Controller.SringSecurity6.UserData.CustomUserDetails;
import com.example.GraduationThesis.Model.Enitity.Users.Roles.ERole;
import com.example.GraduationThesis.Model.Enitity.Users.Roles.Roles;
import com.example.GraduationThesis.Model.Enitity.Users.Users;
import com.example.GraduationThesis.Model.PayLoad.User.Authentication.Login.LoginRequest;
import com.example.GraduationThesis.Model.PayLoad.User.Authentication.Login.LoginResponse;
import com.example.GraduationThesis.Model.PayLoad.User.Authentication.SignUp.SignupRequest;
import com.example.GraduationThesis.Service.API.InterfaceService.AdminAndUser.Authentication.AuthenticationServiceAPI;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.User.RoleService;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.User.UserService;
import com.example.GraduationThesis.Service.LazySingleton.Password.PasswordManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.GraduationThesis.Service.Utils.Status.*;

@Service("AuthenticationImplementation")
public class AuthenticationImplementation implements AuthenticationServiceAPI {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JsonWebTokenProvider jsonWebTokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Override
    public ResponseEntity<String> signUp(SignupRequest signupRequest) {

        if (userService.existsByUsername(signupRequest.getUserName())) {
            return ResponseEntity.badRequest().body("Name is duplicated");
        }

        if (userService.existsBynumberPhone(signupRequest.getNumberPhone())) {
            return ResponseEntity.badRequest().body("Number phone is duplicated");

        }

        if (userService.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email is duplicated");

        }
        Users user = new Users();

        user.setEmail(signupRequest.getEmail());
        user.setNumberPhone(signupRequest.getNumberPhone());
        user.setUsername(signupRequest.getUserName());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        Set<Roles> listRoles = new HashSet<>();
        Roles userRole = roleService.findByRoleName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error : Role user is not found"));

        listRoles.add(userRole);

        user.setListRoles(listRoles);

        userService.save(user);
        return ResponseEntity.ok(new String("User signed up successfully!"));
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            // authenticate requested user information
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // Set authentication information into Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            // return jwt for user
            String jwt = jsonWebTokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());

            List<String> listRoles = customUserDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority()).collect(Collectors.toList());

            // Special line code for Java Swing
            PasswordManager.getInstance().setPassword(customUserDetails.getPassword());

            return new LoginResponse(jwt,
                    "Bearer",
                    customUserDetails.getUser().getEmail(),
                    customUserDetails.getUser().getNumberPhone(),
                    customUserDetails.getUsername(),
                    listRoles.toString(),
                    LOGIN_SUCCESSFUL.toString());

        } catch (BadCredentialsException e) {

            // Handle incorrect username or password
            if (userService.existsByUsername(loginRequest.getUsername())) {
                return new LoginResponse(null, null, null, null, null, null, LOGIN_FAILED_WRONG_PASSWORD.toString());
            } else {
                return new LoginResponse(null, null, null, null, null, null, LOGIN_FAILED_WRONG_USERNAME.toString());
            }
        }
    }
}

