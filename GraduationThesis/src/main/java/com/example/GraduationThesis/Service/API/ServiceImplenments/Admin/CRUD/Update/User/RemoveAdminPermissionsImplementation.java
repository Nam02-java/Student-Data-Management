package com.example.GraduationThesis.Service.API.ServiceImplenments.Admin.CRUD.Update.User;

import com.example.GraduationThesis.Model.Enitity.Users.Roles.ERole;
import com.example.GraduationThesis.Model.Enitity.Users.Users;
import com.example.GraduationThesis.Model.PayLoad.Student.UpdateStudent.UpdateStudentRequest;
import com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Update.AdminServiceUpdateAPI;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service("RemoveAdminPermissionsImplementation")
public class RemoveAdminPermissionsImplementation implements AdminServiceUpdateAPI {

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<?> updateStudentByID(UpdateStudentRequest updateStudentRequest) {
        return null;
    }

    @Override
    public ResponseEntity<?> removeAdminPermissions(@PathVariable String numberphone) {

        Users user = userService.findBynumberPhone(numberphone);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with numberphone: " + numberphone);
        }

        // check if the user hasn't admin rights
        if (!userService.hasAdminRole(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with numberphone: " + numberphone + " does not have admin permissions (ROLE_ADMIN)");
        }

        // remove ROLE_ADMIN
        /**
         * RemoveIf() is a method in Java Collection Framework introduced from Java 8.
         * It is used to remove elements from a Collection (like ArrayList, LinkedList, HashSet, etc.) that satisfy a certain condition. identified by a Predicate.
         * A predicate is an interface in Java that describes a function containing a boolean condition,
         * which accepts an input object and returns true or false depending on whether the condition is true or not.
         */
        user.getListRoles().removeIf(role -> role.getRoleName() == ERole.ROLE_ADMIN);
        userService.save(user); //

        return ResponseEntity.ok("Admin permissions (ROLE_ADMIN) removed for user with numberphone: " + numberphone);
    }
}
