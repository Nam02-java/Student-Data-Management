package com.example.GraduationThesis.Service.API.ServiceImplenments.AdminAndUser.CRUD.Read;

import com.example.GraduationThesis.Controller.SringSecurity6.UserData.CustomUserDetails;
import com.example.GraduationThesis.Model.Enitity.Student.Student;
import com.example.GraduationThesis.Model.Enitity.Users.Roles.ERole;
import com.example.GraduationThesis.Model.Enitity.Users.Users;
import com.example.GraduationThesis.Service.API.InterfaceService.AdminAndUser.CRUD.Read.InformationUser;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.Student.StudentService;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("QueryInformationUserDataImplementation")
public class QueryInformationUserDataImplementation implements InformationUser {

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @Override
    public ResponseEntity<Object> queryInformationUserData() {

        /**
         * get numberphone from current user (ROLE_USER && ROLE_ADMIN) in this session
         * jwt + userdetails + authentication
         */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Users user = userDetails.getUser();

        Map<String, Object> userInfo = new LinkedHashMap<>();
        userInfo.put("ID", user.getId());
        userInfo.put("Username", user.getUsername());
        userInfo.put("Numberphone", user.getNumberPhone());
        userInfo.put("Email", user.getEmail());


        // get list roles and sort by ID role
        List<Map<String, Object>> rolesList = user.getListRoles().stream()
                .filter(role -> role.getRoleId() >= 1 && role.getRoleId() <= 2)
                .sorted((r1, r2) -> Integer.compare(r1.getRoleId(), r2.getRoleId())) // Sort roles by id role
                .map(role -> {
                    Map<String, Object> roleInfo = new LinkedHashMap<>();
                    roleInfo.put("Role ID", role.getRoleId());
                    roleInfo.put("Role Name", role.getRoleName().toString());
                    return roleInfo;
                })
                .collect(Collectors.toList());

        userInfo.put("ListRoles", rolesList);

        if (user.getListRoles().stream().anyMatch(role -> role.getRoleName() == ERole.ROLE_ADMIN)) {
            userInfo.put("My Child", "You can control all student data");
            return ResponseEntity.ok(userInfo);
        }

        List<Object> myChild = getMyChildInfo(user);
        userInfo.put("My Child", myChild);


        return ResponseEntity.ok(userInfo);
    }


    private List<Object> getMyChildInfo(Users user) {
        String currentUserPhoneNumber = user.getNumberPhone();
        List<Student> students = studentService.getStudentsByPartensnumberphone(currentUserPhoneNumber);

        return students.stream()
                .map(student -> {
                    Map<String, Object> studentInfo = new LinkedHashMap<>();
                    studentInfo.put("ID", student.getId());
                    studentInfo.put("Student Name", student.getUsername());
                    studentInfo.put("Email", student.getEmail());
                    studentInfo.put("Birth of Date", student.getDateOfBirth());
                    studentInfo.put("Address", student.getAddress());
                    studentInfo.put("Number Phone", student.getNumberphone());
                    studentInfo.put("Parents Name", student.getPartentsname());
                    studentInfo.put("Parents Number Phone", student.getPartensnumberphone());
                    return studentInfo;
                })
                .collect(Collectors.toList());
    }
}