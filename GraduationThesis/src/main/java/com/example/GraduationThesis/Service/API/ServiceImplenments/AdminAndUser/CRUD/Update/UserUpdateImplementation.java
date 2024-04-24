package com.example.GraduationThesis.Service.API.ServiceImplenments.AdminAndUser.CRUD.Update;

import com.example.GraduationThesis.Controller.SringSecurity6.UserData.CustomUserDetails;
import com.example.GraduationThesis.Model.Enitity.Users.Users;
import com.example.GraduationThesis.Model.PayLoad.User.UpdateUser.UpdateUserRequest;
import com.example.GraduationThesis.Service.API.InterfaceService.AdminAndUser.CRUD.Update.UserServiceUpdateAPI;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Service("UserUpdateImplementation")
public class UserUpdateImplementation implements UserServiceUpdateAPI {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> userUpdate(@RequestBody UpdateUserRequest updateUserRequest) {

        /**
         * get numberphone from current user (ROLE_USER && ROLE_ADMIN) in this session
         * jwt + userdetails + authentication
         */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String currentUserPhoneNumber = userDetails.getUser().getNumberPhone();

        Users user = userService.findBynumberPhone(currentUserPhoneNumber);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("your numberphone not found");
        }


        Map<String, String> updates = updateUserRequest.getUpdates();
        if (updates != null) {
            for (Map.Entry<String, String> entry : updates.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if ("username".equals(key)) {
                    Users existingUser = userService.findByUsername(value);
                    if (existingUser != null && !existingUser.getId().equals(user.getId())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
                    }
                }

                if ("email".equals(key)) {
                    Users existingUser = userService.findByEmail(value);
                    if (existingUser != null && !existingUser.getId().equals(user.getId())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
                    }
                }

                if ("numberphone".equals(key)) {
                    Users existingUser = userService.findBynumberPhone(value);
                    if (existingUser != null && !existingUser.getId().equals(user.getId())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Numberphone already exists");
                    }
                }
            }
        }

        updateUserInformation(user, updateUserRequest.getUpdates());

        userService.save(user);

        return ResponseEntity.ok("update data successfully");
    }

    private void updateUserInformation(Users user, Map<String, String> updates) {
        if (updates != null) {
            updates.forEach((key, value) -> {
                switch (key) {
                    case "username":
                        user.setUsername(value);
                        break;
                    case "password":
                        user.setPassword(passwordEncoder.encode(value));
                        break;
                    case "email":
                        user.setEmail(value);
                        break;
                    case "numberphone":
                        user.setNumberPhone(value);
                        break;
                }
            });
        }
    }
}

