package com.example.GraduationThesis.Service.API.ServiceImplenments.User.CRUD.Read;

import com.example.GraduationThesis.Controller.SringSecurity6.UserData.CustomUserDetails;
import com.example.GraduationThesis.Model.Enitity.Student.Student;
import com.example.GraduationThesis.Model.Enitity.Student.Subject.Scores;
import com.example.GraduationThesis.Service.API.InterfaceService.User.CRUD.Read.UserServiceReadAPI;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.Student.StudentService;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("UserQueryGeneralInformationDataImplenmentation")
public class UserQueryGeneralInformationDataImplenmentation implements UserServiceReadAPI {

    @Autowired
    private UserService userService;
    @Autowired
    private StudentService studentService;


    @Override
    public ResponseEntity<Object> queryGeneralInformationData() {
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
                    studentMap.put("Class Name", student.getClassname());
                    studentMap.put("Position", student.getPosition());
                    studentMap.put("Teacher Name", student.getTeachername());
                    studentMap.put("Address", student.getAddress());
                    studentMap.put("Number Phone", student.getNumberphone());

                    List<Double> overallScores = student.getScores().stream()
                            .map(Scores::getScoreOverall)
                            .filter(Objects::nonNull)
                            .map(Double::parseDouble)
                            .sorted()
                            .collect(Collectors.toList());

                    // Calculate the average score of 13 subjects
                    double averageScore = overallScores.stream()
                            .mapToDouble(Double::doubleValue) // Chuyển đổi thành số thực
                            .average()
                            .orElse(0.0);

                    studentMap.put("GPA", averageScore);

                    return studentMap;
                })
                .sorted(Comparator.comparingDouble(student -> (double) student.get("GPA")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseData);
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
        return null;
    }
}
