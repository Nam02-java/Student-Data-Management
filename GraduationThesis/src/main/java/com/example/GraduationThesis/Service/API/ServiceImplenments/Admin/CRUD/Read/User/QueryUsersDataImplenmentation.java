package com.example.GraduationThesis.Service.API.ServiceImplenments.Admin.CRUD.Read.User;

import com.example.GraduationThesis.Model.Enitity.Student.Student;
import com.example.GraduationThesis.Model.Enitity.Users.Users;
import com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Read.AdminServiceReadAPI;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.Student.StudentService;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("QueryUsersDataImplenmentation")
public class QueryUsersDataImplenmentation implements AdminServiceReadAPI {

    @Autowired
    private UserService userService;
    @Autowired
    private StudentService studentService;

    @Override
    public List<Object> queryGeneralInformationData() {
        return null;
    }

    @Override
    public List<Object> queryPositionInClassData() {
        return null;
    }

    @Override
    public List<Object> queryScoresForSubjectsData() {
        return null;
    }

    @Override
    public List<Object> queryConductScoreData() {
        return null;
    }

    @Override
    public List<Object> queryPersonalInformationData() {
        return null;
    }

    @Override
    public List<Object> queryUsersData() {
        return userService.getAllUsers().stream()
                .map(users -> {
                    Map<String, Object> userInfo = new LinkedHashMap<>();
                    userInfo.put("ID", users.getId());
                    userInfo.put("Username", users.getUsername());
                    userInfo.put("Numberphone", users.getNumberPhone());
                    userInfo.put("Email", users.getEmail());

                    // get list roles and sort by ID role
                    List<Map<String, Object>> rolesList = users.getListRoles().stream()
                            .filter(role -> role.getRoleId() >= 1 && role.getRoleId() <= 2)
                            .sorted((r1, r2) -> Integer.compare(r1.getRoleId(), r2.getRoleId())) // Sort roles by ID of role
                            .map(role -> {
                                Map<String, Object> roleInfo = new LinkedHashMap<>();
                                roleInfo.put("Role ID", role.getRoleId());
                                roleInfo.put("Role Name", role.getRoleName().toString());
                                return roleInfo;
                            })
                            .collect(Collectors.toList());

                    userInfo.put("Roles", rolesList);

                    List<Object> theirChild = getTheirChildInfo(users);
                    userInfo.put("Their Child", theirChild);

                    return userInfo;
                })
                .collect(Collectors.toList());
    }

    private List<Object> getTheirChildInfo(Users user) {
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
