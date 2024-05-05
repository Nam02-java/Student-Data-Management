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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.*;
import java.util.stream.Collectors;

@Service("AuthenticationImplementation")
@Validated
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
    public ResponseEntity<String> signUp( SignupRequest signupRequest) {
//
//        if (signupRequest.getEmail() == null) {
//            signupRequest.setEmail(""); // hoặc có thể sử dụng một giá trị mặc định khác
//        }

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

        // authenticate requested user information
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        /**
         * If no exception occurs, the information is valid
         * Set authentication information into Security Context
         */
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        // return jwt for user
        String jwt = jsonWebTokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());

        List<String> listRoles = customUserDetails.getAuthorities().stream()
                .map(item -> item.getAuthority()).collect(Collectors.toList());

        /**
         * special line code for java swing
         */
        PasswordManager.getInstance().setPassword(customUserDetails.getPassword());

        return new LoginResponse(jwt,
                "Bearer",
                customUserDetails.getUser().getEmail(),
                customUserDetails.getUser().getNumberPhone(),
                customUserDetails.getUsername(),
                listRoles.toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}

