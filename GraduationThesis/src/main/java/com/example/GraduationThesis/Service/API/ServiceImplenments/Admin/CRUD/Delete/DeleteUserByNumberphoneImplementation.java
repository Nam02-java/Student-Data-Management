package com.example.GraduationThesis.Service.API.ServiceImplenments.Admin.CRUD.Delete;

import com.example.GraduationThesis.Model.Enitity.Users.Users;
import com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Delete.AdminServiceDeleteAPI;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service("DeleteUserByNumberphoneImplementation")
public class DeleteUserByNumberphoneImplementation implements AdminServiceDeleteAPI {

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<?> deleteStudentById(Long studentId) {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteUserByNumberphone(@PathVariable String numberphone) {
        Users user = userService.findBynumberPhone(numberphone);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        /**
         * this method will delete all information of student (information , scores and conduct)
         * don't need create deleteSubjects , deleteConduct in services
         */
        userService.deleteById(user.getId());

        return ResponseEntity.ok("User deleted successfully");
    }
}
