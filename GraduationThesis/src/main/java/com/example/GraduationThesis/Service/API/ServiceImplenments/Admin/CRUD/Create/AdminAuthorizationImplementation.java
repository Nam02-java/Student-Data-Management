package com.example.GraduationThesis.Service.API.ServiceImplenments.Admin.CRUD.Create;

import com.example.GraduationThesis.Model.Enitity.Users.Roles.ERole;
import com.example.GraduationThesis.Model.Enitity.Users.Roles.Roles;
import com.example.GraduationThesis.Model.Enitity.Users.Users;
import com.example.GraduationThesis.Model.PayLoad.Student.SignUpStudent.StudenRequest;
import com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Create.AdminServiceCreateAPI;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.User.RoleService;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service("AdminAuthorizationImplementation")
public class AdminAuthorizationImplementation implements AdminServiceCreateAPI {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Override
    public ResponseEntity<?> adminAuthorization(@PathVariable String numberphone) {

        // get name of current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        Roles adminRole = roleService.findByRoleName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error : Role admin is not found"));

        Users user = userService.findBynumberPhone(numberphone);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with numberphone " + numberphone);
        }

        // check if the user has admin rights
        if (userService.hasAdminRole(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with numberphone " + numberphone + " already have admin permissions (ROLE_ADMIN)");
        }

        // update admin role
        user.getListRoles().add(adminRole);

        //save to database
        userService.save(user);

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            user = userService.findByUsername(username);

            /**
             * WARNING : SPECIAL LINE CODE !
             * remove ROLE_ADMIN of current user has ROLE_ADMIN
             */
            user.getListRoles().remove(adminRole);

            userService.save(user);

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to determine current username");
        }

        return ResponseEntity.ok(new String("Update admin role successfully for user has numberphone ") + numberphone +
                " and Admin permissions (ROLE_ADMIN) removed for user with user name " + username + " has numberphone " + user.getNumberPhone());
    }


    @Override
    public ResponseEntity<?> registerStudent(StudenRequest studenRequest) {
        return null;
    }
}
