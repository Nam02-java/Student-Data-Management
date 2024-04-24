package com.example.GraduationThesis.Service.API.ServiceImplenments.User.CRUD.Read;

import com.example.GraduationThesis.Controller.SringSecurity6.UserData.CustomUserDetails;
import com.example.GraduationThesis.Model.Enitity.Student.Student;
import com.example.GraduationThesis.Service.API.InterfaceService.User.CRUD.Read.UserServiceReadAPI;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.Student.StudentService;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("UserQueryPersonalInformationDataImplenmentation")
public class UserQueryPersonalInformationDataImplenmentation implements UserServiceReadAPI {

    @Autowired
    private UserService userService;
    @Autowired
    private StudentService studentService;

    @Override
    public ResponseEntity<Object> queryGeneralInformationData() {
        return null;
    }

    @Override
    public ResponseEntity<Object> queryPositionInClassData() {
        return null;
    }

    @Override
    public ResponseEntity<Object> queryScoresForSubjectsData() {
        return null;
    }

    @Override
    public ResponseEntity<Object> queryConductScoreData() {
        return null;
    }

    @Override
    public ResponseEntity<Object> queryPersonalInformationData() {
        /**
         * get numberphone from current user in this session
         * jwt + userdetails + authentication
         */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String currentUserPhoneNumber = userDetails.getUser().getNumberPhone();

        List<Student> students = studentService.getStudentsByPartensnumberphone(currentUserPhoneNumber);

        if (students.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Numberphone of user not match with student's partens numberphone");
        }

        List<Object> responseData = students.stream()
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

        return ResponseEntity.ok(responseData);
    }
}
