package com.example.GraduationThesis.Service.API.ServiceImplenments.User.CRUD.Read;

import com.example.GraduationThesis.Controller.SringSecurity6.UserData.CustomUserDetails;
import com.example.GraduationThesis.Model.Enitity.Student.Conduct.Conduct;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("UserQueryConductScoreDataImplenmentation")
public class UserQueryConductScoreDataImplenmentation implements UserServiceReadAPI {
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
                    Map<String, Object> studentMap = new LinkedHashMap<>();
                    studentMap.put("ID", student.getId());
                    studentMap.put("Student Name", student.getUsername());

                    List<Map<String, String>> conductInfoList = new ArrayList<>();
                    for (Conduct conduct : student.getConducts()) {
                        Map<String, String> conductMap = new LinkedHashMap<>();
                        conductMap.put("School_Year", conduct.getSchool_year());
                        conductMap.put("Conduct", conduct.getConduct());
                        conductMap.put("Attendance_Score", conduct.getAttendance_Score());
                        conductInfoList.add(conductMap);
                    }
                    studentMap.put("Conducts", conductInfoList);

                    return studentMap;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseData);
    }

    @Override
    public ResponseEntity<Object> queryPersonalInformationData() {
        return null;
    }
}
