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

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("UserUpdateImplementation")
public class UserUpdateImplementation implements UserServiceUpdateAPI {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> userUpdate(UpdateUserRequest updateUserRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String currentUserPhoneNumber = userDetails.getUser().getNumberPhone();

        Users user = userService.findBynumberPhone(currentUserPhoneNumber);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Your numberphone not found");
        }

        Map<String, String> updates = updateUserRequest.getUpdates();
        if (updates != null) {
            for (Map.Entry<String, String> entry : updates.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if ("email".equals(key)) {
                    if (!isValidEmail(value)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
                    }
                    Users existingUser = userService.findByEmail(value);
                    if (existingUser != null && !existingUser.getId().equals(user.getId())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
                    }
                }

                if ("numberphone".equals(key)) {
                    if (!isValidPhoneNumber(value)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid phone number format");
                    }
                    Users existingUser = userService.findBynumberPhone(value);
                    if (existingUser != null && !existingUser.getId().equals(user.getId())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Numberphone already exists");
                    }
                }

                if ("username".equals(key)) {
                    if (value.length() < 6 || value.length() > 30) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username must have 6 to 30 characters");
                    }
                    Users existingUser = userService.findByUsername(value);
                    if (existingUser != null && !existingUser.getId().equals(user.getId())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
                    }
                }

                if ("password".equals(key)) {
                    if (value.length() < 8) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password must have at least 8 characters");
                    }
                }
            }
        }

        updateUserInformation(user, updateUserRequest.getUpdates());

        userService.save(user);

        return ResponseEntity.ok("Update data successfully");
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

    private boolean isValidEmail(String email) {
        String regex = "^[\\w.-]+@gmail\\.com$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("\\d{10,11}");
    }
}
