package com.example.GraduationThesis.Controller.APICollection.AdminAndUser.CRUD.Update;

import com.example.GraduationThesis.Model.PayLoad.User.UpdateUser.UpdateUserRequest;
import com.example.GraduationThesis.Service.API.InterfaceService.AdminAndUser.CRUD.Update.UserServiceUpdateAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public")
public class UserUpdateAPI {
    @Autowired
    @Qualifier("UserUpdateImplementation")
    private UserServiceUpdateAPI userServiceUpdateAPI;

    @PutMapping("/updateUser")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateStudentByID(@RequestBody UpdateUserRequest updateUserRequest) {
        return userServiceUpdateAPI.userUpdate(updateUserRequest);
    }
}
